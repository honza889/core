package org.jboss.as.console.client.shared.subsys.jsmpolicy.server;

import org.jboss.as.console.client.core.SuspendableViewImpl;
import org.jboss.as.console.client.layout.OneToOneLayout;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DetailView extends SuspendableViewImpl implements DetailPresenter.MyView
{
    private VerticalPanel container = new VerticalPanel();
    private TextArea textArea = new TextArea();

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
                .setDescription("Here you can see policy used on selected server.")
                .setMaster("Policy of the server", container);

        textArea.setReadOnly(true);
        textArea.setHeight("200px");
        container.add(textArea);

        return panel.build();
    }

    public void setPolicyFile(String policyFile)
    {
        textArea.setText(policyFile);
    }
}