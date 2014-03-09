package org.jboss.as.console.client.shared.subsys.jsmpolicy.policies;

import java.util.List;
import java.util.Map;

import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.layout.FormLayout;
import org.jboss.as.console.client.layout.MultipleToOneLayout;
import org.jboss.as.console.client.shared.help.FormHelpPanel;
import org.jboss.as.console.client.shared.subsys.Baseadress;
import org.jboss.as.console.client.widgets.forms.FormToolStrip;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.TextAreaItem;
import org.jboss.ballroom.client.widgets.forms.TextItem;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;
import org.jboss.ballroom.client.widgets.tools.ToolButton;
import org.jboss.ballroom.client.widgets.tools.ToolStrip;
import org.jboss.ballroom.client.widgets.window.Feedback;
import org.jboss.dmr.client.ModelNode;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

public class PolicyEditor {

    private PoliciesPresenter presenter;
    private Form<PolicyEntity> form;
    private ListDataProvider<PolicyEntity> dataProvider;
    private DefaultCellTable<PolicyEntity> table;

    public PolicyEditor(PoliciesPresenter presenter) {
        this.presenter = presenter;
    }

    @SuppressWarnings("unchecked")
    Widget asWidget() {

        table = new DefaultCellTable<PolicyEntity>(8, new ProvidesKey<PolicyEntity>() {
            public Object getKey(PolicyEntity item) {
                return item.getName();
            }
        });
        dataProvider = new ListDataProvider<PolicyEntity>();
        dataProvider.addDataDisplay((HasData<PolicyEntity>)table);

        TextColumn<PolicyEntity> nameColumn = new TextColumn<PolicyEntity>() {
            public String getValue(PolicyEntity record) {
                return record.getName();
            }
        };
        table.addColumn(nameColumn, "Policy name");

        ToolStrip toolstrip = new ToolStrip();

        ToolButton addBtn = new ToolButton(Console.CONSTANTS.common_label_add(), new ClickHandler() {
            public void onClick(ClickEvent event) {
                presenter.launchNewSessionWizard();
            }
        });
        toolstrip.addToolButtonRight(addBtn);

        ToolButton removeBtn = new ToolButton(Console.CONSTANTS.common_label_delete(), new ClickHandler() {
            public void onClick(ClickEvent event) {
                Feedback.confirm(
                        Console.MESSAGES.deleteTitle("JSM policy file"),
                        Console.MESSAGES.deleteConfirm("JSM policy file"),
                        new Feedback.ConfirmationHandler() {
                            public void onConfirmation(boolean isConfirmed) {
                                if (isConfirmed)
                                    presenter.onDelete(form.getEditedEntity());
                            }
                        });
            }
        });
        toolstrip.addToolButtonRight(removeBtn);

        form = new Form<PolicyEntity>(PolicyEntity.class){
            /*
            public FormValidation validate(){
                return new FormValidation(); // always without errors, because '\n'
            }
            */
        };
        form.setNumColumns(2);

        TextItem nameItem = new TextItem("name", "Policy name");
        NewlinedTextAreaItem fileItem = new NewlinedTextAreaItem("file", "File content", 15);

        form.setFields(nameItem, fileItem);
        form.setEnabled(false);

        FormHelpPanel helpPanel = new FormHelpPanel(new FormHelpPanel.AddressCallback() {
            public ModelNode getAddress() {
                ModelNode address = Baseadress.get();
                address.add("subsystem", "jsmpolicy");
                address.add("policy", "*");
                return address;
            }
        }, form);

        FormToolStrip<PolicyEntity> formToolStrip = new FormToolStrip<PolicyEntity>(
                form, new FormToolStrip.FormCallback<PolicyEntity>() {
            public void onSave(Map<String, Object> changeset) {

                Console.error("Debug PolicyEditor.onSave: file", (String)changeset.get("file"));

                presenter.onSave(form.getEditedEntity(), changeset);
            }
            public void onDelete(PolicyEntity entity) {

            }
        });

        Widget detail = new FormLayout()
                       .setForm(form)
                       .setHelp(helpPanel)
                       .build();

        Widget panel = new MultipleToOneLayout()
                .setPlain(true)
                .setTitle("Policies")
                .setHeadline("JSM Policies")
                .setDescription(new SafeHtmlBuilder().appendEscaped("Java security manager policies definition.").toSafeHtml())
                .setMaster(Console.MESSAGES.available("JSM Policies"), table)
                .setMasterTools(toolstrip.asWidget())
                .setDetailTools(formToolStrip.asWidget())
                .setDetail(Console.CONSTANTS.common_label_selection(), detail).build();

        form.bind(table);

        return panel;
    }

    public void updateFrom(List<PolicyEntity> list) {
        dataProvider.setList(list);
        table.selectDefaultEntity();
    }

    public static class NewlinedTextAreaItem extends TextAreaItem {

        public NewlinedTextAreaItem(String name, String title, int lines) {
            super(name, title);
            setVisibleLines(lines);
        }

        @Override
        public String getValue() {
            return textArea.getValue().replace("\n", "\\n");
        }

        @Override
        public boolean validate(String value) {
            return true;
        }

    }

}
