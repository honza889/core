package org.jboss.as.console.client.shared.subsys.jsmpolicy;

import java.util.Map;

import org.jboss.as.console.client.Console;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.google.gwt.user.client.Command;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.TreeViewModel;

public class JsmTreeViewModel implements TreeViewModel {
	
	Map<String,JsmNode> serverGroups;
	ListDataProvider<JsmNode> dataProvider = new ListDataProvider<JsmNode>();
	private static Command finishCmd = null;
	
	public JsmTreeViewModel(Map<String,JsmNode> serverGroups){
		this.serverGroups = serverGroups;
	}
	
	public <T> NodeInfo<?> getNodeInfo(T value) {
		
		final ListDataProvider<JsmNode> dataProvider = new ListDataProvider<JsmNode>();
		
		if (value instanceof JsmNode) {
			for(JsmNode server : ((JsmNode)value).getNodes()){
				dataProvider.getList().add(server);
			}
		}else{
			setFinish(new Command() {
                public void execute() {
                	dataProvider.getList().clear();
                	for(JsmNode group : serverGroups.values()){
    					dataProvider.getList().add(group);
    				}
                }
            });
		}
		
		return new DefaultNodeInfo<JsmNode>(dataProvider, new NodeCell());
	}
	
	public boolean isLeaf(Object value) {
		return (value instanceof JsmNode) && ((JsmNode)value).getNodes().isEmpty();
	}
	
	public class NodeCell extends AbstractCell<JsmNode> {
		public void render(Cell.Context context, JsmNode value, SafeHtmlBuilder sb) {
			sb.append(SimpleSafeHtmlRenderer.getInstance().render(value.getName()));
		}
	}
	
	public static void setFinish(Command finishCmd) {
		JsmTreeViewModel.finishCmd = finishCmd;
	}
	
	public static void runFinish() {
		if(finishCmd != null) finishCmd.execute();
	}
	
}
