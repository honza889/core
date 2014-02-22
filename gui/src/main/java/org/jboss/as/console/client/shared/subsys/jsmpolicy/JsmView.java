package org.jboss.as.console.client.shared.subsys.jsmpolicy;

import com.google.gwt.user.client.ui.Widget;

import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.core.DisposableViewImpl;
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

    @Override
    public Widget createWidget() {

        form = new Form<JsmServer>(JsmServer.class);
        form.setNumColumns(2);

        TextBoxItem defaultDs = new TextBoxItem("defaultDataSource", "Default Datasource", false);
        ComboBoxItem inheritance = new ComboBoxItem("inheritance", "Persistence Inheritance")
        {
            @Override
            public boolean isRequired() {
                return false;
            }
        };

        inheritance.setValueMap(new String[] {"DEEP", "SHALLOW"});

        form.setFields(defaultDs, inheritance);
        form.setEnabled(false);
        
        FormToolStrip<JsmServer> formToolStrip = new FormToolStrip<JsmServer>(
                form, new FormToolStrip.FormCallback<JsmServer>() {
            @Override
            public void onSave(Map<String, Object> changeset) {
                presenter.onSave(form.getEditedEntity(), changeset);
            }
            @Override
            public void onDelete(JsmServer entity) {
                // cannot be removed
            }
        });
        formToolStrip.providesDeleteOp(false);
        
        FormHelpPanel helpPanel = new FormHelpPanel(new FormHelpPanel.AddressCallback() {
            @Override
            public ModelNode getAddress() {
                ModelNode address = Baseadress.get();
                address.add("subsystem", "jpa");
                return address;
            }
        }, form);

        Widget detail = new FormLayout()
                .setForm(form)
                .setHelp(helpPanel).build();

        Widget panel = new OneToOneLayout()
                .setTitle("JSM Policies")
                .setHeadline("JSM Policy Subsystem")
                .setDescription(Console.CONSTANTS.subsys_jpa_desc())
                .setMaster("Details", detail)
                .setMasterTools(formToolStrip.asWidget()).build();



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
}
