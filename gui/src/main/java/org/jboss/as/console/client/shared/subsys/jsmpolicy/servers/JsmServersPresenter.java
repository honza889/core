package org.jboss.as.console.client.shared.subsys.jsmpolicy.servers;

import static org.jboss.dmr.client.ModelDescriptionConstants.ADD;
import static org.jboss.dmr.client.ModelDescriptionConstants.ADDRESS;
import static org.jboss.dmr.client.ModelDescriptionConstants.CHILD_TYPE;
import static org.jboss.dmr.client.ModelDescriptionConstants.NAME;
import static org.jboss.dmr.client.ModelDescriptionConstants.OP;
import static org.jboss.dmr.client.ModelDescriptionConstants.READ_ATTRIBUTE_OPERATION;
import static org.jboss.dmr.client.ModelDescriptionConstants.READ_CHILDREN_NAMES_OPERATION;
import static org.jboss.dmr.client.ModelDescriptionConstants.VALUE;
import static org.jboss.dmr.client.ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.core.message.Message;
import org.jboss.as.console.client.domain.model.HostInformationStore;
import org.jboss.as.console.client.domain.model.LoggingCallback;
import org.jboss.as.console.client.domain.model.ServerInstance;
import org.jboss.as.console.client.domain.model.SimpleCallback;
import org.jboss.as.console.client.domain.topology.HostInfo;
import org.jboss.as.console.client.shared.subsys.Baseadress;
import org.jboss.as.console.client.shared.subsys.RevealStrategy;
import org.jboss.as.console.spi.AccessControl;
import org.jboss.as.console.spi.SubsystemExtension;
import org.jboss.dmr.client.ModelDescriptionConstants;
import org.jboss.dmr.client.ModelNode;
import org.jboss.dmr.client.dispatch.DispatchAsync;
import org.jboss.dmr.client.dispatch.impl.DMRAction;
import org.jboss.dmr.client.dispatch.impl.DMRResponse;

import com.allen_sauer.gwt.log.client.Log;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.Proxy;

public class JsmServersPresenter extends Presenter<JsmServersPresenter.MyView, JsmServersPresenter.MyProxy> {

	private RevealStrategy revealStrategy;
	private HostInformationStore hostStore;
	private DispatchAsync dispatcher;

	@ProxyCodeSplit
	@NameToken("jsmpolicy-servers")
	@AccessControl(resources = {
            "{selected.profile}/subsystem=jsmpolicy"
    })
	@SubsystemExtension(name = "Servers", group = "JSM Policy", key = "jsmpolicy")
	public interface MyProxy extends Proxy<JsmServersPresenter>, Place {}
	public interface MyView extends View {
        void setServerGroups(Map<String,JsmNode> serverGroups);
        void refresh();
        List<JsmPolicy> getPolicyPossibleValues();
	}

	@Inject
	public JsmServersPresenter(EventBus eventBus, MyView view, MyProxy proxy, RevealStrategy revealStrategy, HostInformationStore hostStore, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		this.revealStrategy = revealStrategy;
		this.hostStore = hostStore;
		this.dispatcher = dispatcher;
	}

	protected void onReset() {
        super.onReset();
        loadServerGroups();
        loadPolicyPossibleValues();
    }

	private void loadServerGroups() {
		    ModelNode operation = new ModelNode();
            operation.get(ADDRESS).set(Baseadress.get());
            operation.get(OP).set(READ_ATTRIBUTE_OPERATION);
            operation.get(NAME).set("launch-type");
            dispatcher.execute(new DMRAction(operation), new LoggingCallback<DMRResponse>() {
                public void onSuccess(DMRResponse response) {
                    String result = response.get().get(ModelDescriptionConstants.RESULT).asString();
                    if(result.equalsIgnoreCase("STANDALONE")){
                        loadServerGroupsInStandalone();
                    }else{
                        loadServerGroupsInDomain();
                    }
                }
            });
	}

	private void loadServerGroupsInStandalone() {
	    final JsmServersPresenter presenter = this;
	    ModelNode operation = new ModelNode();
        operation.get(ADDRESS).set(Baseadress.get());
        operation.get(OP).set(READ_ATTRIBUTE_OPERATION);
        operation.get(NAME).set("name");
        dispatcher.execute(new DMRAction(operation), new LoggingCallback<DMRResponse>() {
            public void onSuccess(DMRResponse response) {
                String serverName = response.get().get(ModelDescriptionConstants.RESULT).asString();

                Map<String, JsmNode> serverGroups = new HashMap<String, JsmNode>();

                JsmNode serverNode = new JsmNode(serverName, presenter);
                loadServerPolicy(serverNode);
                serverGroups.put(serverName, serverNode);

                getView().setServerGroups(serverGroups);
            }
        });
    }

    private void loadServerGroupsInDomain() {

        final JsmServersPresenter presenter = this;
        hostStore.loadHostsAndServerInstances(new SimpleCallback<List<HostInfo>>() {
            public void onSuccess(List<HostInfo> hosts) {
                try {
                    Map<String, JsmNode> serverGroups = new HashMap<String, JsmNode>();
                    for (HostInfo host : hosts) {
                        for (ServerInstance instance : host.getServerInstances()) {
                            String groupName = instance.getGroup();
                            String serverName = instance.getServer();
                            JsmNode serverNode = new JsmNode(serverName, presenter);
                            loadServerPolicy(serverNode);

                            if (serverGroups.containsKey(groupName)) {
                                serverGroups.get(groupName).getNodes().add(serverNode);
                            } else {
                                JsmNode groupNode = new JsmNode(groupName, presenter);
                                groupNode.getNodes().add(serverNode);
                                serverGroups.put(groupName, groupNode);
                            }
                        }
                    }
                    getView().setServerGroups(serverGroups);
                } catch (Exception e) {
                    Console.error("Exception after server groups loading", e.getMessage());
                }
            }
        });
	}

    private void loadServerPolicy(final JsmNode serverNode) {

        ModelNode operation = new ModelNode();
        operation.get(ADDRESS).set(Baseadress.get());
        operation.get(ADDRESS).add("subsystem", "jsmpolicy");
        operation.get(ADDRESS).add("server", serverNode.getName());
        operation.get(OP).set(READ_ATTRIBUTE_OPERATION);
        operation.get(NAME).set("policy");

        dispatcher.execute(new DMRAction(operation), new LoggingCallback<DMRResponse>() {
            public void onSuccess(DMRResponse response) {
                String result = response.get().get(ModelDescriptionConstants.RESULT).asString();
                serverNode.initPolicy(result);
                getView().refresh();
            }
            public void onFailure(Throwable caught) {
                // server not defined - policy will be null
                serverNode.initPolicy(null);
                getView().refresh();
            }
        });
    }

	public void setServerPolicy(final String server, final String policy){

        // write attribute
        ModelNode operation = new ModelNode();
        operation.get(ADDRESS).set(Baseadress.get());
        operation.get(ADDRESS).add("subsystem", "jsmpolicy");
        operation.get(ADDRESS).add("server", server);
        operation.get(NAME).set("policy");
        if (policy == null) {
            operation.get(OP).set("undefine-attribute");
        } else {
            operation.get(OP).set(WRITE_ATTRIBUTE_OPERATION);
            operation.get(VALUE).set(policy);
        }

        dispatcher.execute(new DMRAction(operation), new LoggingCallback<DMRResponse>() {
            public void onSuccess(DMRResponse response) {
                Console.info("Policy of server " + server + " changed to " + policy);
                getView().refresh();
            }

            public void onFailure(final Throwable caught2) {
                // add node
                final ModelNode operation = new ModelNode();
                operation.get(ADDRESS).set(Baseadress.get());
                operation.get(ADDRESS).add("subsystem", "jsmpolicy");
                operation.get(ADDRESS).add("server", server);
                operation.get(OP).set(ADD);
                if (policy != null) {
                    operation.get("policy").set(policy);
                }

                dispatcher.execute(new DMRAction(operation), new LoggingCallback<DMRResponse>() {
                    public void onSuccess(DMRResponse response) {
                        Console.info("Policy of server " + server + " set to " + policy);
                        getView().refresh();
                    }
                    public void onFailure(Throwable caught1) {
                        Console.error("Failure setting server policy",
                                      caught1.getLocalizedMessage() + "\n" +
                                      caught2.getLocalizedMessage());
                    }
                });
            }
        });

	}

	public void loadPolicyPossibleValues(){

	    final List<JsmPolicy> policyPossibleValues = getView().getPolicyPossibleValues();

	    ModelNode operation = new ModelNode();
	    operation.get(ADDRESS).set(Baseadress.get());
        operation.get(ADDRESS).add("subsystem", "jsmpolicy");
        operation.get(OP).set(READ_CHILDREN_NAMES_OPERATION);
        operation.get(CHILD_TYPE).set("policy");

        dispatcher.execute(new DMRAction(operation), new LoggingCallback<DMRResponse>() {
            public void onSuccess(DMRResponse response) {

                List<ModelNode> children = response.get().get(ModelDescriptionConstants.RESULT).asList();

                policyPossibleValues.clear();
                policyPossibleValues.add(new JsmPolicy(null,null));

                for(final ModelNode child : children){

                    ModelNode operation = new ModelNode();
                    operation.get(ADDRESS).set(Baseadress.get());
                    operation.get(ADDRESS).add("subsystem", "jsmpolicy");
                    operation.get(ADDRESS).add("policy", child.asString());
                    operation.get(OP).set(READ_ATTRIBUTE_OPERATION);
                    operation.get(NAME).set("file");

                    dispatcher.execute(new DMRAction(operation), new LoggingCallback<DMRResponse>() {
                        public void onSuccess(DMRResponse response) {

                            String file = response.get().get(ModelDescriptionConstants.RESULT).asString();
                            policyPossibleValues.add(new JsmPolicy(child.asString(), file));

                        }
                    });
                }

                getView().refresh();
            }
            public void onFailure(Throwable caught) {
                Console.error("Failure loading possible policies", caught.getLocalizedMessage());
            }
        });

	}

	protected void revealInParent() {
		revealStrategy.revealInParent(this);
	}
}
