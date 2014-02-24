package org.jboss.as.console.client.shared.subsys.jsmpolicy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.domain.model.HostInformationStore;
import org.jboss.as.console.client.domain.model.ServerGroupStore;
import org.jboss.as.console.client.domain.model.ServerInstance;
import org.jboss.as.console.client.domain.model.SimpleCallback;
import org.jboss.as.console.client.domain.topology.HostInfo;
import org.jboss.as.console.client.shared.subsys.RevealStrategy;
import org.jboss.as.console.client.shared.viewframework.FrameworkView;
import org.jboss.as.console.spi.SubsystemExtension;
import org.jboss.as.console.spi.AccessControl;

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
	private ServerGroupStore serverGroupStore;
	private HostInformationStore hostStore;
	
	@ProxyCodeSplit
	@NameToken("jsmpolicy")
	@AccessControl(resources = {
            "{selected.profile}/subsystem=jsmpolicy"
    })
	@SubsystemExtension(name = "JSM Policies", group = "JSM Policy", key = "jsmpolicy")
	//@RuntimeExtension(name="JSM POLICY", key="jsmpolicy")
	public interface MyProxy extends Proxy<JsmPresenter>, Place {}
	public interface MyView extends View, FrameworkView {
        void setServerGroups(Map<String,JsmNode> serverGroups);
	}
	
	@Inject
	public JsmPresenter(EventBus eventBus, MyView view, MyProxy proxy, RevealStrategy revealStrategy, ServerGroupStore serverGroupStore, HostInformationStore hostStore) {
		super(eventBus, view, proxy);
		this.revealStrategy = revealStrategy;
		this.serverGroupStore = serverGroupStore;
		this.hostStore = hostStore;
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
								
								if(serverGroups.containsKey(groupName)){
									serverGroups.get(groupName).getNodes().add(new JsmNode(serverName, false));
								}else{
									JsmNode groupNode = new JsmNode(groupName, true);
									groupNode.getNodes().add(new JsmNode(serverName, false));
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
