package org.jboss.as.console.client.shared.subsys.jsmpolicy.policies;

import org.jboss.as.console.client.shared.help.FormHelpPanel;
import org.jboss.as.console.client.shared.subsys.Baseadress;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.FormValidation;
import org.jboss.ballroom.client.widgets.forms.TextBoxItem;
import org.jboss.ballroom.client.widgets.window.DialogueOptions;
import org.jboss.ballroom.client.widgets.window.WindowContentBuilder;
import org.jboss.dmr.client.ModelNode;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class NewPolicyWizard {

    private final PoliciesPresenter presenter;

    public NewPolicyWizard(final PoliciesPresenter presenter) {
        this.presenter = presenter;
    }

    Widget asWidget() {
        VerticalPanel layout = new VerticalPanel();
        layout.setStyleName("window-content");

        final Form<PolicyEntity> form = new Form<PolicyEntity>(PolicyEntity.class);
        form.setFields(new TextBoxItem("name", "Policy name"));

        DialogueOptions options = new DialogueOptions(
                new ClickHandler() { // save
                    public void onClick(ClickEvent event) {
                        FormValidation validation = form.validate();
                        if(validation.hasErrors()) return;
                        presenter.onCreateSession(form.getUpdatedEntity());
                    }
                },
                new ClickHandler() { // cancel
                    public void onClick(ClickEvent event) {
                        presenter.closeDialoge();
                    }
                }
        );

        // ----------------------------------------

        Widget formWidget = form.asWidget();
        final FormHelpPanel helpPanel = new FormHelpPanel(
                new FormHelpPanel.AddressCallback() {
                    public ModelNode getAddress() {
                        ModelNode address = Baseadress.get();
                        address.add("subsystem", "jsmpolicy");
                        address.add("policy", "*");
                        return address;
                    }
                }, form
        );

        layout.add(helpPanel.asWidget());
        layout.add(formWidget);
        return new WindowContentBuilder(layout, options).build();
    }
}
