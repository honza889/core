package org.jboss.as.console.client.shared.subsys.jsmpolicy.servers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class NodeCell extends AbstractInputCell<Node, Node> {

    interface Template extends SafeHtmlTemplates {
      @Template("<option value=\"{0}\">{1}</option>")
      SafeHtml deselected(String value, String name);

      @Template("<option value=\"{0}\" selected=\"selected\">{1}</option>")
      SafeHtml selected(String value, String name);
    }

    private static Template template;

    private HashMap<String, Integer> indexForOption = new HashMap<String, Integer>();

    private final List<Policy> options;

    public NodeCell(List<Policy> options) {
      super(BrowserEvents.CHANGE);
      if (template == null) {
        template = GWT.create(Template.class);
      }
      this.options = new ArrayList<Policy>(options);
      int index = 0;
      for (Policy option : options) {
        indexForOption.put(option.getName(), index++);
      }
    }

    public void onBrowserEvent(Context context, Element parent, Node value,
        NativeEvent event, ValueUpdater<Node> valueUpdater) {
      super.onBrowserEvent(context, parent, value, event, valueUpdater);
      String type = event.getType();
      if (BrowserEvents.CHANGE.equals(type)) {
        Object key = context.getKey();
        SelectElement select = parent.getChild(1).cast();

        value.setPolicy(options.get(select.getSelectedIndex()).getName());

        setViewData(key, value);
        finishEditing(parent, value, key, valueUpdater);
        if (valueUpdater != null) {
          valueUpdater.update(value);
        }
      }
    }

    public void render(Context context, Node value, SafeHtmlBuilder sb) {

      Object key = context.getKey();
      Node viewData = getViewData(key);
      if (viewData != null && viewData.equals(value)) {
        clearViewData(key);
        viewData = null;
      }

      sb.appendHtmlConstant("<span style=\"display:inline-block;width:250px\">");
      sb.appendEscaped(value.getName());
      sb.appendHtmlConstant("</span>");

      int selectedIndex = getSelectedIndex(viewData == null ? value : viewData);
      sb.appendHtmlConstant("<select tabindex=\"-1\" style=\"width:400px\">");
      int index = 0;
      for (Policy option : options) {
          String optionName = option.getName() == null ? "none" : option.getName();
          String optionValue = option.getName() == null ? "" : option.getName();
          if (index++ == selectedIndex) {
              sb.append(template.selected(optionValue, optionName));
          } else {
              sb.append(template.deselected(optionValue, optionName));
          }
      }
      sb.appendHtmlConstant("</select>");
    }

    private int getSelectedIndex(Node value) {
      Integer index = indexForOption.get(value.getPolicy());
      return (index == null) ? -1 : index.intValue();
    }
}