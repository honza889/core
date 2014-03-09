package org.jboss.as.console.client.shared.subsys.jsmpolicy.policies;

import java.util.List;
import java.util.Map;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.core.NameTokens;
import org.jboss.as.console.client.layout.FormLayout;
import org.jboss.as.console.client.layout.MultipleToOneLayout;
import org.jboss.as.console.client.shared.help.FormHelpPanel;
import org.jboss.as.console.client.shared.subsys.Baseadress;
import org.jboss.as.console.client.widgets.forms.FormToolStrip;
import org.jboss.as.console.client.widgets.tables.ViewLinkCell;
import org.jboss.ballroom.client.widgets.forms.CheckBoxItem;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.TextBoxItem;
import org.jboss.ballroom.client.widgets.forms.TextItem;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;
import org.jboss.ballroom.client.widgets.tools.ToolButton;
import org.jboss.ballroom.client.widgets.tools.ToolStrip;
import org.jboss.ballroom.client.widgets.window.Feedback;
import org.jboss.dmr.client.ModelNode;

/**
 * @author Heiko Braun
 * @date 2/14/12
 */
public class JsmPoliciesSessionEditor {

    private JsmPoliciesPresenter presenter;
    private Form<JsmPoliciesSession> form;
    private ListDataProvider<JsmPoliciesSession> dataProvider;
    private DefaultCellTable<JsmPoliciesSession> table ;

    public JsmPoliciesSessionEditor(JsmPoliciesPresenter presenter) {
        this.presenter = presenter;
    }

    Widget asWidget() {

        table = new DefaultCellTable<JsmPoliciesSession>(8, new ProvidesKey<JsmPoliciesSession>() {
            @Override
            public Object getKey(JsmPoliciesSession item) {
                return item.getName();
            }
        });
        dataProvider = new ListDataProvider<JsmPoliciesSession>();
        dataProvider.addDataDisplay(table);

        TextColumn<JsmPoliciesSession> jndiName = new TextColumn<JsmPoliciesSession>() {
            @Override
            public String getValue(JsmPoliciesSession record) {
                return record.getJndiName();
            }
        };

        Column<JsmPoliciesSession, JsmPoliciesSession> option = new Column<JsmPoliciesSession, JsmPoliciesSession>(
                new ViewLinkCell<JsmPoliciesSession>(Console.CONSTANTS.common_label_view(), new ActionCell.Delegate<JsmPoliciesSession>() {
                    @Override
                    public void execute(JsmPoliciesSession selection) {
                        presenter.getPlaceManager().revealPlace(
                                new PlaceRequest.Builder().nameToken(NameTokens.MailPresenter)
                                        .with("name", selection.getName()).build());
                    }
                })
        ) {
            @Override
            public JsmPoliciesSession getValue(JsmPoliciesSession manager) {
                return manager;
            }
        };


        table.addColumn(jndiName, "JNDI Name");
        table.addColumn(option, "Option");

        ToolStrip toolstrip = new ToolStrip();

        ToolButton addBtn = new ToolButton(Console.CONSTANTS.common_label_add(), new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                presenter.launchNewSessionWizard();
            }
        });
        addBtn.ensureDebugId(Console.DEBUG_CONSTANTS.debug_label_add_mailSessionView());
        toolstrip.addToolButtonRight(addBtn);

        ToolButton removeBtn = new ToolButton(Console.CONSTANTS.common_label_remove(), new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Feedback.confirm(
                        Console.MESSAGES.deleteTitle("Mail Session"),
                        Console.MESSAGES.deleteConfirm("Mail Session"),
                        new Feedback.ConfirmationHandler() {
                            @Override
                            public void onConfirmation(boolean isConfirmed) {
                                if (isConfirmed)
                                    presenter.onDelete(form.getEditedEntity());
                            }
                        });
            }
        });
        removeBtn.ensureDebugId(Console.DEBUG_CONSTANTS.debug_label_remove_mailSessionView());
        toolstrip.addToolButtonRight(removeBtn);

        // ------


        form = new Form<JsmPoliciesSession>(JsmPoliciesSession.class);
        form.setNumColumns(2);

        TextItem jndi = new TextItem("jndiName", "JNDI Name");
        CheckBoxItem debug = new CheckBoxItem("debug", "Debug Enabled?");
        TextBoxItem from = new TextBoxItem("from", "Default From", false);

        form.setFields(jndi, debug, from);
        form.setEnabled(false);


        FormHelpPanel helpPanel = new FormHelpPanel(new FormHelpPanel.AddressCallback() {
            @Override
            public ModelNode getAddress() {
                ModelNode address = Baseadress.get();
                address.add("subsystem", "mail");
                address.add("mail-session", "*");
                return address;
            }
        }, form);

        FormToolStrip<JsmPoliciesSession> formToolStrip = new FormToolStrip<JsmPoliciesSession>(
                form, new FormToolStrip.FormCallback<JsmPoliciesSession>() {
            @Override
            public void onSave(Map<String, Object> changeset) {
                presenter.onSave(form.getEditedEntity(), changeset);
            }

            @Override
            public void onDelete(JsmPoliciesSession entity) {

            }
        });
        formToolStrip.providesDeleteOp(false);

        Widget detail = new FormLayout()
                       .setForm(form)
                       .setHelp(helpPanel).build();

        Widget panel = new MultipleToOneLayout()
                .setPlain(true)
                .setTitle("Mail")
                .setHeadline("Mail Sessions")
                .setDescription(Console.CONSTANTS.subsys_mail_session_desc())
                .setMaster(Console.MESSAGES.available("Mail Session"), table)
                .setMasterTools(toolstrip.asWidget())
                .setDetailTools(formToolStrip.asWidget())
                .setDetail(Console.CONSTANTS.common_label_selection(), detail).build();

        form.bind(table);



        return panel;

    }

    public void updateFrom(List<JsmPoliciesSession> list) {
        dataProvider.setList(list);

        table.selectDefaultEntity();
    }
}
