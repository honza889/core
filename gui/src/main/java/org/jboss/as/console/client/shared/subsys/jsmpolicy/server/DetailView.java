package org.jboss.as.console.client.shared.subsys.jsmpolicy.server;

import org.jboss.as.console.client.core.SuspendableViewImpl;
import org.jboss.as.console.client.layout.OneToOneLayout;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DetailView extends SuspendableViewImpl implements DetailPresenter.MyView
{
    private VerticalPanel container = new VerticalPanel();
    private TextBox textBoxName = new TextBox();
    private TextArea textAreaContent = new TextArea();

    @Override
    public void setPresenter(DetailPresenter presenter) {

    }

    @Override
    public Widget createWidget()
    {
        container.setStyleName("fill-layout");

        OneToOneLayout panel = new OneToOneLayout()
                .setTitle("JSM Policy")
                .setHeadline("JSM Policy of the server")
                .setDescription("Java Security Manager policy file used on this server.")
                .setMaster("Policy of the server", container);

        textBoxName.setReadOnly(true);
        container.add(textBoxName);

        textAreaContent.setReadOnly(true);
        textAreaContent.setHeight("200px");
        container.add(textAreaContent);

        return panel.build();
    }

    public void setPolicyFile(String policyFileName, String policyFileContent)
    {
        textBoxName.setText(policyFileName);
        textAreaContent.setText(policyFileContent);
    }
}