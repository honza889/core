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
import org.jboss.as.console.client.shared.state.DomainEntityManager;
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

public class PoliciesPresenter extends Presenter<PoliciesPresenter.MyView, PoliciesPresenter.MyProxy> {

    private final PlaceManager placeManager;
    private final RevealStrategy revealStrategy;
    private final DispatchAsync dispatcher;
    private final EntityAdapter<PolicyEntity> adapter;
    private final BeanMetaData beanMetaData;
    private final DomainEntityManager domainManager;

    private DefaultWindow window;

    @ProxyCodeSplit
    @NameToken("jsmpolicy-policies")
    @AccessControl(resources = {
            "{selected.profile}/subsystem=jsmpolicy"
    })
    @SubsystemExtension(name = "Policies", group = "JSM Policy", key = "jsmpolicy")
    public interface MyProxy extends Proxy<PoliciesPresenter>, Place {}

    public interface MyView extends View {
        void setPresenter(PoliciesPresenter presenter);
        void updateFrom(List<PolicyEntity> list);
    }

    @Inject
    public PoliciesPresenter(EventBus eventBus, MyView view, MyProxy proxy, PlaceManager placeManager,
            DispatchAsync dispatcher, RevealStrategy revealStrategy, ApplicationMetaData metaData,
            DomainEntityManager domainManager) {

        super(eventBus, view, proxy);

        this.placeManager = placeManager;
        this.revealStrategy = revealStrategy;
        this.dispatcher = dispatcher;
        this.domainManager = domainManager;
        this.beanMetaData = metaData.getBeanMetaData(PolicyEntity.class);
        this.adapter = new EntityAdapter<PolicyEntity>(PolicyEntity.class, metaData);
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
        loadPoliciesList();
    }

    public void launchNewSessionWizard() {
        window = new DefaultWindow(Console.MESSAGES.createTitle("JSM Policy"));
        window.setWidth(480);
        window.setHeight(360);
        window.trapWidget(new NewPolicyWizard(PoliciesPresenter.this).asWidget());
        window.setGlassEnabled(true);
        window.center();
    }

    private void loadPoliciesList() {
        ModelNode operation = beanMetaData.getAddress().asSubresource(Baseadress.get());
        operation.get(OP).set(READ_CHILDREN_RESOURCES_OPERATION);
        operation.get(RECURSIVE).set(true);

        dispatcher.execute(new DMRAction(operation), new SimpleCallback<DMRResponse>() {
            @Override
            public void onSuccess(DMRResponse result) {
                ModelNode response = result.get();

                if (response.isFailure()) {
                    Console.error(Console.MESSAGES.failed("JSM Policies"));
                } else {
                    List<Property> items = response.get(RESULT).asPropertyList();
                    List<PolicyEntity> sessions = new ArrayList<PolicyEntity>(items.size());
                    for (Property item : items) {
                        ModelNode model = item.getValue();
                        PolicyEntity policy = adapter.fromDMR(model);
                        policy.setName(item.getName());
                        sessions.add(policy);
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

    public void onCreateSession(final PolicyEntity entity) {
        closeDialoge();

        ModelNode address = beanMetaData.getAddress().asResource(Baseadress.get(), entity.getName());

        ModelNode operation = adapter.fromEntity(entity);
        operation.get(ADDRESS).set(address.get(ADDRESS));
        operation.get(OP).set(ADD);

        dispatcher.execute(new DMRAction(operation), new SimpleCallback<DMRResponse>() {
            @Override
            public void onSuccess(DMRResponse result) {
                ModelNode response  = result.get();
                if (response.isFailure()) {
                    Console.error(Console.MESSAGES.addingFailed("JSM Policy"), response.getFailureDescription());
                } else {
                    Console.info(Console.MESSAGES.added("JSM Policy " + entity.getName()));
                }
                loadPoliciesList();
            }
        });
    }

    public void onDelete(final PolicyEntity entity) {
        ModelNode operation = beanMetaData.getAddress().asResource(Baseadress.get(), entity.getName());
        operation.get(OP).set(REMOVE);

        dispatcher.execute(new DMRAction(operation), new SimpleCallback<DMRResponse>() {
            @Override
            public void onSuccess(DMRResponse result) {
                ModelNode response  = result.get();
                if (response.isFailure()) {
                    Console.error(Console.MESSAGES.deletionFailed("JSM Policy"), response.getFailureDescription());
                } else {
                    Console.info(Console.MESSAGES.deleted("JSM Policy " + entity.getName()));
                }
                loadPoliciesList();
            }
        });
    }

    public void onSave(final PolicyEntity editedEntity, Map<String, Object> changeset) {

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
                    Console.error(Console.MESSAGES.modificationFailed("JSM Policy"),
                            response.getFailureDescription());
                } else {
                    Console.info(Console.MESSAGES.modified("JSM Policy " + editedEntity.getName()));
                }
                loadPoliciesList();
            }
        });
    }
}
