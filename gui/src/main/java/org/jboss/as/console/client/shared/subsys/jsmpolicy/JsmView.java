package org.jboss.as.console.client.shared.subsys.jsmpolicy;

import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import org.jboss.as.console.client.core.DisposableViewImpl;
import org.jboss.as.console.client.layout.OneToOneLayout;
import org.jboss.ballroom.client.widgets.forms.Form;

import java.util.Map;

public class JsmView extends DisposableViewImpl implements JsmPresenter.MyView {
	
	private Form<JsmServer> form;
	private VerticalPanel container;
    
    public Widget createWidget() {
    	
    	container = new VerticalPanel();
    	container.setStyleName("fill-layout");
    	
    	OneToOneLayout panel = new OneToOneLayout()
				.setTitle("JSM Policies")
				.setHeadline("JSM Policy Subsystem")
				.setDescription("This subsystem allow set policy used by Java Security Manager.")
				.setMaster("Servers by server group", container);
    	
		return panel.build();
    }

	public void setServerGroups(Map<String,JsmNode> serverGroups) {
		
		JsmTreeViewModel model = new JsmTreeViewModel(serverGroups);
		CellTree tree = new CellTree(model,"Domain");
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
    
	public void initialLoad() {
		// TODO Auto-generated method stub
	}
	
	public void refresh() {
		// TODO Auto-generated method stub
	}
	
	public void setEditingEnabled(boolean isEnabled) {
		// TODO Auto-generated method stub
	}
	
}
