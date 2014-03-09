package org.jboss.as.console.client.shared.subsys.jsmpolicy.policies;

import java.util.List;

import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.core.DisposableViewImpl;
import org.jboss.as.console.client.widgets.pages.PagedView;
import org.jboss.ballroom.client.widgets.tabs.FakeTabPanel;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Heiko Braun
 * @date 11/28/11
 */
public class JsmPoliciesView extends DisposableViewImpl implements JsmPoliciesPresenter.MyView{

    private JsmPoliciesPresenter presenter;
    private PagedView panel;
    private JsmPoliciesSessionEditor sessionEditor;
    private List<JsmPoliciesSession> sessions;

    @Override
    public Widget createWidget() {


        LayoutPanel layout = new LayoutPanel();

        FakeTabPanel titleBar = new FakeTabPanel("Mail");
        layout.add(titleBar);

        panel = new PagedView();

        sessionEditor = new JsmPoliciesSessionEditor(presenter);

        panel.addPage(Console.CONSTANTS.common_label_back(), sessionEditor.asWidget());
        panel.showPage(0);

        Widget panelWidget = panel.asWidget();
        layout.add(panelWidget);

        layout.setWidgetTopHeight(titleBar, 0, Style.Unit.PX, 40, Style.Unit.PX);
        layout.setWidgetTopHeight(panelWidget, 40, Style.Unit.PX, 100, Style.Unit.PCT);

        return layout;
    }

    @Override
    public void setPresenter(JsmPoliciesPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void updateFrom(List<JsmPoliciesSession> list) {
        this.sessions = list;
        sessionEditor.updateFrom(list);
    }
}
