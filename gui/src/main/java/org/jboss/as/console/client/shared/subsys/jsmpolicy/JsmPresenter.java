package org.jboss.as.console.client.shared.subsys.jsmpolicy;

import static org.jboss.dmr.client.ModelDescriptionConstants.ADDRESS;
import static org.jboss.dmr.client.ModelDescriptionConstants.NAME;
import static org.jboss.dmr.client.ModelDescriptionConstants.OP;
import static org.jboss.dmr.client.ModelDescriptionConstants.READ_ATTRIBUTE_OPERATION;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.domain.model.HostInformationStore;
import org.jboss.as.console.client.domain.model.LoggingCallback;
import org.jboss.as.console.client.domain.model.ServerInstance;
import org.jboss.as.console.client.domain.model.SimpleCallback;
import org.jboss.as.console.client.domain.topology.HostInfo;
import org.jboss.as.console.client.shared.subsys.Baseadress;
import org.jboss.as.console.client.shared.subsys.RevealStrategy;
import org.jboss.as.console.client.shared.viewframework.FrameworkView;
import org.jboss.as.console.client.widgets.forms.ApplicationMetaData;
import org.jboss.as.console.client.widgets.forms.EntityAdapter;
import org.jboss.as.console.spi.AccessControl;
import org.jboss.as.console.spi.SubsystemExtension;
import org.jboss.dmr.client.ModelDescriptionConstants;
import org.jboss.dmr.client.ModelNode;
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
import com.gwtplatform.mvp.client.proxy.Proxy;

public class JsmPresenter extends Presenter<JsmPresenter.MyView, JsmPresenter.MyProxy> {

	private RevealStrategy revealStrategy;
	private HostInformationStore hostStore;
	private final EntityAdapter<JsmServer> serverAdapter;
	private DispatchAsync dispatcher;

	@ProxyCodeSplit
	@NameToken("jsmpolicy")
	@AccessControl(resources = {
            "{selected.profile}/subsystem=jsmpolicy"
    })
	@SubsystemExtension(name = "JSM Policies", group = "JSM Policy", key = "jsmpolicy")
	//@RuntimeExtension(name="JSM POLICY", key="jsmpolicy")
	public interface MyProxy extends Proxy<JsmPresenter>, Place {}
	public interface MyView extends View, FrameworkView { // TODO: remove FrameworkView?
        void setServerGroups(Map<String,JsmNode> serverGroups);
	}

	@Inject
	public JsmPresenter(EventBus eventBus, MyView view, MyProxy proxy, RevealStrategy revealStrategy, HostInformationStore hostStore, ApplicationMetaData metaData, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		this.revealStrategy = revealStrategy;
		this.hostStore = hostStore;
		this.dispatcher = dispatcher;
		this.serverAdapter = new EntityAdapter<JsmServer>(JsmServer.class, metaData);
	}

	protected void onReset() {
        super.onReset();

        loadServerGroups();

        getView().initialLoad();
    }

	private void loadServerGroups() {
		try {
	        hostStore.loadHostsAndServerInstances(new SimpleCallback<List<HostInfo>>() {
				public void onSuccess(List<HostInfo> hosts) {
					try {
						Map<String,JsmNode> serverGroups = new HashMap<String,JsmNode>();
						for (HostInfo host : hosts) {
							for (ServerInstance instance : host.getServerInstances()){
								String groupName = instance.getGroup();
								String serverName = instance.getServer();
								JsmNode serverNode = new JsmNode(serverName, false);
								loadServerPolicy(serverNode);

								if(serverGroups.containsKey(groupName)){
									serverGroups.get(groupName).getNodes().add(serverNode);
								}else{
									JsmNode groupNode = new JsmNode(groupName, true);
									groupNode.getNodes().add(serverNode);
									serverGroups.put(groupName, groupNode);
								}
							}
						}
						getView().setServerGroups(serverGroups);
					}
					catch(Exception e) {
						Console.error("Exception after server groups loading", e.getMessage());
					}
				}

				public void loadServerPolicy(final JsmNode serverNode){

				    ModelNode operation = new ModelNode();
			        operation.get(ADDRESS).set(Baseadress.get());
			        operation.get(ADDRESS).add("subsystem", "jsmpolicy");
			        operation.get(ADDRESS).add("server", serverNode.getName());
			        operation.get(OP).set(READ_ATTRIBUTE_OPERATION);
			        operation.get(NAME).set("policy");

			        dispatcher.execute(new DMRAction(operation), new LoggingCallback<DMRResponse>() {
			            public void onSuccess(DMRResponse response) {

			                String result = response.get().get(ModelDescriptionConstants.RESULT).asString();
			                serverNode.setPolicy(result);

			                getView().refresh();
			            }
			            public void onFailure(Throwable caught) {
			                // server not defined
			                serverNode.setPolicy(null); // TODO: "" ?
			                getView().refresh();
			            }
			        });

				}
			});
		}
		catch(Exception e) {
			Console.error("Exception before server groups loading", e.getMessage());
		}
	}

	protected void revealInParent() {
		revealStrategy.revealInParent(this);
	}

	public void onSave(JsmServer editedEntity, Map<String, Object> changeset) {
		/*
        ModelNode operation = adapter.fromChangeset(changeset, beanMetaData.getAddress().asResource(Baseadress.get()));

        if(changeset.containsKey("defaultDataSource") && changeset.get("defaultDataSource").equals(""))
        {
            changeset.remove("defaultDataSource");
            operation.get("default-datasource").set(ModelType.UNDEFINED);
        }

        // TODO: https://issues.jboss.org/browse/AS7-3596
        dispatcher.execute(new DMRAction(operation), new SimpleCallback<DMRResponse>() {
            @Override
            public void onSuccess(DMRResponse result) {
                ModelNode response  = result.get();

                if(response.isFailure())
                {
                    Console.error(Console.MESSAGES.modificationFailed("JPA Subsystem"), response.getFailureDescription());
                }
                else
                {
                    Console.info(Console.MESSAGES.modified("JPA Subsystem"));
                }

                loadSubsystem();
            }
        });
        */
    }
}
