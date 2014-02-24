package org.jboss.as.console.client.shared.subsys.jsmpolicy;

import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.core.DisposableViewImpl;
import org.jboss.as.console.client.layout.OneToOneLayout;
import org.jboss.ballroom.client.layout.RHSContentPanel;
import org.jboss.ballroom.client.widgets.ContentHeaderLabel;
import org.jboss.ballroom.client.widgets.forms.Form;

import java.util.List;
import java.util.Map;

public class JsmView extends DisposableViewImpl implements JsmPresenter.MyView {

    private Form<JsmServer> form;
    private Map<String,JsmNode> serverGroups = null;
    
    private CellTree tree;
    private OneToOneLayout panel;
    
    private VerticalPanel container = new VerticalPanel();
    
    @Override
    public Widget createWidget() {
    	/*
		TreeViewModel model = new JsmTreeViewModel(serverGroups);
    	tree = new CellTree(model,"Domain");
    	tree.setStyleName("jndi-tree");
    	tree.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);
    	*/
    	panel = new OneToOneLayout()
				.setTitle("JSM Policies")
				.setHeadline("JSM Policy Subsystem")
				.setDescription("This subsystem allow set policy used by Java Security Manager.")
				.setMaster("Servers by server group", container);
    	
		return panel.build();
    	
    	/*
    	RHSContentPanel layout = new RHSContentPanel(Console.CONSTANTS.subsys_naming_jndiView());
    	container = new VerticalPanel();
        container.setStyleName("fill-layout");
        layout.add(new ContentHeaderLabel(Console.CONSTANTS.subsys_naming_jndiBindings()));
        layout.add(container);
        return layout;
        */
    }

	public void setServerGroups(Map<String,JsmNode> serverGroups) {
		
		this.serverGroups = serverGroups;
		JsmTreeViewModel model = new JsmTreeViewModel(serverGroups);
		tree = new CellTree(model,"Domain");
		JsmTreeViewModel.runFinish();
		
		container.clear();
		container.add(tree);
		
	}
	
	public void clearValues() {
		container.clear();
	}
	
    public void updateFrom(JsmServer s) {
        form.edit(s);
    }
    
	@Override
	public void initialLoad() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setEditingEnabled(boolean isEnabled) {
		// TODO Auto-generated method stub
		
	}
	
	public Map<String,JsmNode> getServerGroups() {
		return serverGroups;
	}

}
