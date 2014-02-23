package org.jboss.as.console.client.shared.subsys.jsmpolicy;

import java.util.List;
import java.util.Map;

import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.domain.model.ServerGroupRecord;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.TreeViewModel;

public class JsmTreeViewModel implements TreeViewModel {
	
	Map<String,JsmNode> serverGroups;
	
	public JsmTreeViewModel(Map<String,JsmNode> serverGroups){
		this.serverGroups = serverGroups;
	}
	
	public <T> NodeInfo<?> getNodeInfo(T value) {
		ListDataProvider<String> dataProvider = new ListDataProvider<String>();
		if(serverGroups != null){
			/*
			if(value == null){
				for(JsmNode group : serverGroups.values()){
					dataProvider.getList().add(group);
				}
			}else{
				dataProvider.setList(((JsmNode)value).getNodes());
			}
			*/
		}else{
			Console.error("No server groups defined", "No server groups was defined.");
		}
		return new DefaultNodeInfo<String>(dataProvider, new TextCell());
	}
	
	@Override
	public boolean isLeaf(Object value) {
		return false;
	}
	
}
