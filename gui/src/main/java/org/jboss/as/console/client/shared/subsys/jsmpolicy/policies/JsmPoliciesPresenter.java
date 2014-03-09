package org.jboss.as.console.client.shared.subsys.jsmpolicy.policies;

import static org.jboss.dmr.client.ModelDescriptionConstants.ADD;
import static org.jboss.dmr.client.ModelDescriptionConstants.ADDRESS;
import static org.jboss.dmr.client.ModelDescriptionConstants.OP;
import static org.jboss.dmr.client.ModelDescriptionConstants.READ_CHILDREN_RESOURCES_OPERATION;
import static org.jboss.dmr.client.ModelDescriptionConstants.RECURSIVE;
import static org.jboss.dmr.client.ModelDescriptionConstants.REMOVE;
import static org.jboss.dmr.client.ModelDescriptionConstants.RESULT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.domain.model.SimpleCallback;
import org.jboss.as.console.client.shared.subsys.Baseadress;
import org.jboss.as.console.client.shared.subsys.RevealStrategy;
import org.jboss.as.console.client.widgets.forms.ApplicationMetaData;
import org.jboss.as.console.client.widgets.forms.BeanMetaData;
import org.jboss.as.console.client.widgets.forms.EntityAdapter;
import org.jboss.as.console.spi.AccessControl;
import org.jboss.as.console.spi.SubsystemExtension;
import org.jboss.ballroom.client.widgets.window.DefaultWindow;
import org.jboss.dmr.client.ModelNode;
import org.jboss.dmr.client.Property;
import org.jboss.dmr.client.dispatch.DispatchAsync;
import org.jboss.dmr.client.dispatch.impl.DMRAction;
import org.jboss.dmr.client.dispatch.impl.DMRResponse;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;

/**
 * @author Heiko Braun
 */
public class JsmPoliciesPresenter extends Presenter<JsmPoliciesPresenter.MyView, JsmPoliciesPresenter.MyProxy> {

    private final PlaceManager placeManager;
    private final RevealStrategy revealStrategy;
    private final DispatchAsync dispatcher;
    private final EntityAdapter<JsmPoliciesSession> adapter;
    private final BeanMetaData beanMetaData;

    private DefaultWindow window;

    @ProxyCodeSplit
    @NameToken("jsmpolicy-policies")
    @AccessControl(resources = {
            "{selected.profile}/subsystem=jsmpolicy"
    })
    @SubsystemExtension(name = "Policies", group = "JSM Policy", key = "jsmpolicy")
    public interface MyProxy extends Proxy<JsmPoliciesPresenter>, Place {}

    public interface MyView extends View {
        void setPresenter(JsmPoliciesPresenter presenter);
        void updateFrom(List<JsmPoliciesSession> list);
    }

    @Inject
    public JsmPoliciesPresenter(EventBus eventBus, MyView view, MyProxy proxy, PlaceManager placeManager,
            DispatchAsync dispatcher, RevealStrategy revealStrategy, ApplicationMetaData metaData) {

        super(eventBus, view, proxy);

        this.placeManager = placeManager;
        this.revealStrategy = revealStrategy;
        this.dispatcher = dispatcher;
        this.beanMetaData = metaData.getBeanMetaData(JsmPoliciesSession.class);
        this.adapter = new EntityAdapter<JsmPoliciesSession>(JsmPoliciesSession.class, metaData);
    }

    @Override
    protected void onBind() {
        super.onBind();
        getView().setPresenter(this);
    }

    public PlaceManager getPlaceManager() {
        return placeManager;
    }

    @Override
    protected void onReset() {
        super.onReset();
        loadMailSessions();
    }

    public void launchNewSessionWizard() {
        window = new DefaultWindow(Console.MESSAGES.createTitle("Mail Session"));
        window.setWidth(480);
        window.setHeight(360);
        window.trapWidget(new NewMailSessionWizard(JsmPoliciesPresenter.this).asWidget());
        window.setGlassEnabled(true);
        window.center();
    }

    private void loadMailSessions() {
        ModelNode operation = beanMetaData.getAddress().asSubresource(Baseadress.get());
        operation.get(OP).set(READ_CHILDREN_RESOURCES_OPERATION);
        operation.get(RECURSIVE).set(true);

        dispatcher.execute(new DMRAction(operation), new SimpleCallback<DMRResponse>() {
            @Override
            public void onSuccess(DMRResponse result) {
                ModelNode response = result.get();

                if (response.isFailure()) {
                    Console.error(Console.MESSAGES.failed("Mail Sessions"));
                } else {
                    List<Property> items = response.get(RESULT).asPropertyList();
                    List<JsmPoliciesSession> sessions = new ArrayList<JsmPoliciesSession>(items.size());
                    for (Property item : items) {
                        ModelNode model = item.getValue();
                        JsmPoliciesSession mailSession = adapter.fromDMR(model);
                        mailSession.setName(item.getName());
                        // TODO: FILE ?
                        sessions.add(mailSession);

                        /*
                        if (model.hasDefined("server")) {
                            List<Property> serverList = model.get("server").asPropertyList();
                            for (Property server : serverList) {
                                if (server.getName().equals(ServerType.smtp.name())) {
                                    MailServer2Definition smtpServer = serverAdapter.fromDMR(server.getValue());
                                    smtpServer.setType(ServerType.smtp);
                                    mailSession.setSmtpServer(smtpServer);
                                } else if (server.getName().equals(ServerType.imap.name())) {
                                    MailServer2Definition imap = serverAdapter.fromDMR(server.getValue());
                                    imap.setType(ServerType.imap);
                                    mailSession.setImapServer(imap);
                                } else if (server.getName().equals(ServerType.pop3.name())) {
                                    MailServer2Definition pop = serverAdapter.fromDMR(server.getValue());
                                    pop.setType(ServerType.pop3);
                                    mailSession.setPopServer(pop);
                                }
                            }

                        }
                        */

                    }
                    getView().updateFrom(sessions);
                }
            }
        });
    }

    @Override
    protected void revealInParent() {
        revealStrategy.revealInParent(this);
    }

    public void closeDialoge() {
        window.hide();
    }

    public void onCreateSession(final JsmPoliciesSession entity) {
        closeDialoge();

        String name = entity.getName() != null ? entity.getName() : entity.getJndiName();
        ModelNode address = beanMetaData.getAddress().asResource(Baseadress.get(), name);

        ModelNode operation = adapter.fromEntity(entity);
        operation.get(ADDRESS).set(address.get(ADDRESS));
        operation.get(OP).set(ADD);

        dispatcher.execute(new DMRAction(operation), new SimpleCallback<DMRResponse>() {
            @Override
            public void onSuccess(DMRResponse result) {
                ModelNode response  = result.get();
                if (response.isFailure()) {
                    Console.error(Console.MESSAGES.addingFailed("Mail Session"), response.getFailureDescription());
                } else {
                    Console.info(Console.MESSAGES.added("Mail Session " + entity.getName()));
                }
                loadMailSessions();
            }
        });
    }

    public void onDelete(final JsmPoliciesSession entity) {
        ModelNode operation = beanMetaData.getAddress().asResource(Baseadress.get(), entity.getName());
        operation.get(OP).set(REMOVE);

        dispatcher.execute(new DMRAction(operation), new SimpleCallback<DMRResponse>() {
            @Override
            public void onSuccess(DMRResponse result) {
                ModelNode response  = result.get();
                if (response.isFailure()) {
                    Console.error(Console.MESSAGES.deletionFailed("Mail Session"), response.getFailureDescription());
                } else {
                    Console.info(Console.MESSAGES.deleted("Mail Session " + entity.getName()));
                }
                loadMailSessions();
            }
        });
    }

    public void onSave(final JsmPoliciesSession editedEntity, Map<String, Object> changeset) {
        ModelNode address = beanMetaData.getAddress().asResource(
                Baseadress.get(),
                editedEntity.getName()
        );
        ModelNode operation = adapter.fromChangeset(changeset, address);

        dispatcher.execute(new DMRAction(operation), new SimpleCallback<DMRResponse>() {
            @Override
            public void onSuccess(DMRResponse result) {
                ModelNode response  = result.get();
                if (response.isFailure()) {
                    Console.error(Console.MESSAGES.modificationFailed("Mail Session"),
                            response.getFailureDescription());
                } else {
                    Console.info(Console.MESSAGES.modified("Mail Session " + editedEntity.getName()));
                }
                loadMailSessions();
            }
        });
    }
}
