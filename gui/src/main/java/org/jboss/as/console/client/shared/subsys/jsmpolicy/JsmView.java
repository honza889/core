package org.jboss.as.console.client.shared.subsys.jsmpolicy;

import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
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
import org.jboss.ballroom.client.widgets.forms.ComboBoxItem;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.TextBoxItem;
import org.jboss.dmr.client.ModelNode;

import java.util.List;
import java.util.Map;

public class JsmView extends DisposableViewImpl implements JsmPresenter.MyView {

    private JsmPresenter presenter;
    private Form<JsmServer> form;
    private List<ServerGroupRecord> serverGroups;
    
    @Override
    public Widget createWidget() {
    	
    	System.err.println("JSM POLICY JsmView.createWidget()");
    	
    	TreeViewModel model = new JsmTreeViewModel(serverGroups);
    	CellTree tree = new CellTree(model,"Domain");
    	tree.setStyleName("jndi-tree");
    	tree.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);
        
        Widget panel = new OneToOneLayout()
                .setTitle("JSM Policies")
                .setHeadline("JSM Policy Subsystem")
                .setDescription(Console.CONSTANTS.subsys_jpa_desc())
                .setMaster("Details", tree)
                .build();
        
        return panel;
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

	public List<ServerGroupRecord> getServerGroups() {
		return serverGroups;
	}

	public void setServerGroups(List<ServerGroupRecord> serverGroups) {
		this.serverGroups = serverGroups;
	}
}
