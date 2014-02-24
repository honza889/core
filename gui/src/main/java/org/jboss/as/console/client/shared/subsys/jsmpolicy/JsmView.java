package org.jboss.as.console.client.shared.subsys.jsmpolicy;

import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.TreeViewModel;

import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.core.DisposableViewImpl;
import org.jboss.as.console.client.domain.model.ServerGroupRecord;
import org.jboss.as.console.client.shared.help.FormHelpPanel;
import org.jboss.as.console.client.shared.subsys.Baseadress;
import org.jboss.as.console.client.shared.subsys.jpa.model.JpaSubsystem;
import org.jboss.as.console.client.shared.subsys.mail.MailPresenter;
import org.jboss.as.console.client.shared.subsys.mail.MailSession;
import org.jboss.as.console.client.layout.FormLayout;
import org.jboss.as.console.client.layout.OneToOneLayout;
import org.jboss.as.console.client.widgets.forms.BlankItem;
import org.jboss.as.console.client.widgets.forms.FormToolStrip;
import org.jboss.ballroom.client.layout.RHSContentPanel;
import org.jboss.ballroom.client.widgets.ContentHeaderLabel;
import org.jboss.ballroom.client.widgets.forms.ComboBoxItem;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.TextBoxItem;
import org.jboss.dmr.client.ModelNode;

import java.util.List;
import java.util.Map;

public class JsmView extends DisposableViewImpl implements JsmPresenter.MyView {

    private JsmPresenter presenter;
    private Form<JsmServer> form;
    private Map<String,JsmNode> serverGroups = null;
    
    private CellTree tree;
    private OneToOneLayout panel;
    
    private VerticalPanel container;
    
    @Override
    public Widget createWidget() {
    	/*
		TreeViewModel model = new JsmTreeViewModel(serverGroups);
    	tree = new CellTree(model,"Domain");
    	tree.setStyleName("jndi-tree");
    	tree.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);
    	
    	panel = new OneToOneLayout()
				.setTitle("JSM Policies")
				.setHeadline("JSM Policy Subsystem")
				.setDescription("This subsystem allow set policy used by Java Security Manager.")
				.setMaster("Details", tree);
    	
		return panel.build();
    	*/
    	
    	RHSContentPanel layout = new RHSContentPanel(Console.CONSTANTS.subsys_naming_jndiView());
    	container = new VerticalPanel();
        container.setStyleName("fill-layout");
        layout.add(new ContentHeaderLabel(Console.CONSTANTS.subsys_naming_jndiBindings()));
        layout.add(container);
        return layout;
        
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
	
    public void setPresenter(JsmPresenter presenter) {
        this.presenter = presenter;
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

	@Override
	public void setPresenter(MailPresenter presenter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateFrom(List<MailSession> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSelectedSession(String selectedSession) {
		// TODO Auto-generated method stub
		
	}

	public Map<String,JsmNode> getServerGroups() {
		return serverGroups;
	}

}
