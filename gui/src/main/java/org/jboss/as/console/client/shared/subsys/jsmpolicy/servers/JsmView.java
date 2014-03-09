package org.jboss.as.console.client.shared.subsys.jsmpolicy.servers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.as.console.client.core.DisposableViewImpl;
import org.jboss.as.console.client.layout.OneToOneLayout;

import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class JsmView extends DisposableViewImpl implements JsmPresenter.MyView {

	private VerticalPanel container;
	private Map<String,JsmNode> serverGroups;
	private final List<JsmPolicy> policyPossibleValues = new ArrayList<JsmPolicy>();

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

		this.serverGroups = serverGroups;

	    JsmTreeViewModel model = new JsmTreeViewModel(serverGroups, policyPossibleValues);
		CellTree tree = new CellTree(model,"Domain");
		JsmTreeViewModel.runFinish();

		container.clear();
		container.add(tree);

	}

	public List<JsmPolicy> getPolicyPossibleValues(){
	    return policyPossibleValues;
	}

	public void refresh() {
		setServerGroups(serverGroups);
	}

}
