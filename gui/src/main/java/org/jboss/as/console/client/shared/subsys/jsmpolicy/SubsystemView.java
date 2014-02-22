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

public class SubsystemView extends DisposableViewImpl implements SubsystemPresenter.MyView {

    private SubsystemPresenter presenter;
    private Form<Server> form;

    @Override
    public Widget createWidget() {

        form = new Form<Server>(Server.class);
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
        
        FormToolStrip<Server> formToolStrip = new FormToolStrip<Server>(
                form, new FormToolStrip.FormCallback<Server>() {
            @Override
            public void onSave(Map<String, Object> changeset) {
                presenter.onSave(form.getEditedEntity(), changeset);
            }
            @Override
            public void onDelete(Server entity) {
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
                .setTitle("JSM")
                .setHeadline("JSM Policy Subsystem")
                .setDescription(Console.CONSTANTS.subsys_jpa_desc())
                .setMaster("Details", detail)
                .setMasterTools(formToolStrip.asWidget()).build();



        return panel;
    }

    public void setPresenter(SubsystemPresenter presenter) {
        this.presenter = presenter;
    }

    public void updateFrom(Server s) {
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
