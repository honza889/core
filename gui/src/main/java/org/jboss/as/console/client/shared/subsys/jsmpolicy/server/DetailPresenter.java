package org.jboss.as.console.client.shared.subsys.jsmpolicy.server;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.Proxy;

import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.core.BootstrapContext;
import org.jboss.as.console.client.core.SuspendableView;
import org.jboss.as.console.client.domain.model.LoggingCallback;
import org.jboss.as.console.spi.AccessControl;
import org.jboss.as.console.spi.RuntimeExtension;
import org.jboss.dmr.client.dispatch.DispatchAsync;
import org.jboss.dmr.client.dispatch.impl.DMRAction;
import org.jboss.dmr.client.dispatch.impl.DMRResponse;
import org.jboss.as.console.client.shared.runtime.RuntimeBaseAddress;
import org.jboss.as.console.client.shared.state.DomainEntityManager;
import org.jboss.as.console.client.shared.state.ServerSelectionChanged;
import org.jboss.as.console.client.shared.subsys.RevealStrategy;
import org.jboss.dmr.client.ModelDescriptionConstants;
import org.jboss.dmr.client.ModelNode;
import org.jboss.dmr.client.ModelType;

import static org.jboss.dmr.client.ModelDescriptionConstants.*;

public class DetailPresenter extends Presenter<DetailPresenter.MyView,
        DetailPresenter.MyProxy> implements ServerSelectionChanged.ChangeListener
{
    @ProxyCodeSplit
    @NameToken("jsmpolicy-detail")
    @AccessControl(resources = {
            "{selected.profile}/subsystem=jsmpolicy"
    })
    @RuntimeExtension(name = "JSM Policy", key = "jsmpolicy")
    public interface MyProxy extends Proxy<DetailPresenter>, Place {}
    public interface MyView extends SuspendableView
    {
        void setPresenter(DetailPresenter presenter);
        void setPolicyFile(String policyFile);
    }

    private final DispatchAsync dispatcher;
    private final RevealStrategy revealStrategy;
    private final BootstrapContext bootstrap;
    private final DomainEntityManager domainManager;


    @Inject
    public DetailPresenter(final EventBus eventBus, final MyView view,
                                final MyProxy proxy, final DispatchAsync dispatcher,
                                final RevealStrategy revealStrategy, final BootstrapContext bootstrap,
                                final DomainEntityManager domainManager)
    {
        super(eventBus, view, proxy);
        this.dispatcher = dispatcher;
        this.revealStrategy = revealStrategy;
        this.bootstrap = bootstrap;
        this.domainManager = domainManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onBind()
    {
        super.onBind();
        getView().setPresenter(this);
        getEventBus().addHandler(ServerSelectionChanged.TYPE, this);
    }

    @Override
    protected void revealInParent()
    {
        revealStrategy.revealInRuntimeParent(this);
    }

    @Override
    protected void onReset()
    {
        super.onReset();
        refresh();
    }


    @Override
    public void onServerSelectionChanged(boolean isRunning) {
        if(isVisible()) refresh();
    }

    public void refresh()
    {
        String serverName = domainManager.getSelectedServer();
        if(serverName.equals(NOT_SET)) return;

        ModelNode operation = new ModelNode();
        operation.get(ADDRESS).set(RuntimeBaseAddress.get());
        operation.get(ADDRESS).add("subsystem", "jsmpolicy");
        operation.get(ADDRESS).add("server", serverName);
        operation.get(OP).set(READ_ATTRIBUTE_OPERATION);
        operation.get(NAME).set("policy");

        dispatcher.execute(new DMRAction(operation), new LoggingCallback<DMRResponse>() {
            public void onSuccess(DMRResponse response) {
                ModelNode resultServer = response.get().get(ModelDescriptionConstants.RESULT);

                if(resultServer.getType()==ModelType.UNDEFINED){

                    ModelNode operation = new ModelNode();
                    operation.get(ADDRESS).set(RuntimeBaseAddress.get());
                    operation.get(OP).set(READ_ATTRIBUTE_OPERATION);
                    operation.get(NAME).set("server-state");

                    dispatcher.execute(new DMRAction(operation), new LoggingCallback<DMRResponse>() {
                        public void onSuccess(DMRResponse response) {
                            String resultState = response.get().get(ModelDescriptionConstants.RESULT).asString();
                            if(resultState.equals("running")){
                                getView().setPolicyFile("(Security manager not used)");
                            }else{
                                getView().setPolicyFile("(Server is "+resultState+")");
                            }
                        }
                        public void onFailure(Throwable caught) {
                            super.onFailure(caught);
                            Console.error("Failure when detecting state of server", caught.getLocalizedMessage());
                        }
                    });
                    return;
                }

                ModelNode operation = new ModelNode();
                operation.get(ADDRESS).set(RuntimeBaseAddress.get());
                operation.get(ADDRESS).add("subsystem", "jsmpolicy");
                operation.get(ADDRESS).add("policy", resultServer.asString());
                operation.get(OP).set(READ_ATTRIBUTE_OPERATION);
                operation.get(NAME).set("file");

                dispatcher.execute(new DMRAction(operation), new LoggingCallback<DMRResponse>() {
                    public void onSuccess(DMRResponse response) {
                        ModelNode resultPolicy = response.get().get(ModelDescriptionConstants.RESULT);
                        getView().setPolicyFile(resultPolicy.asString());
                    }
                    public void onFailure(Throwable caught) {
                        super.onFailure(caught);
                        Console.error("Failure when loading policy node", caught.getLocalizedMessage());
                    }
                });
            }
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
                Console.error("Failure when loading server node", caught.getLocalizedMessage());
            }
        });
    }
}
