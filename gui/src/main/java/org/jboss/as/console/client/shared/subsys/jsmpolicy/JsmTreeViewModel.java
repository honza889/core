package org.jboss.as.console.client.shared.subsys.jsmpolicy;

import java.util.List;

import org.jboss.as.console.client.domain.model.ServerGroupRecord;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.TreeViewModel;

public class JsmTreeViewModel implements TreeViewModel {
	
	List<ServerGroupRecord> serverGroups;
	
	public JsmTreeViewModel(List<ServerGroupRecord> serverGroups){
		this.serverGroups = serverGroups;
	}
	
	public <T> NodeInfo<?> getNodeInfo(T value) {
		ListDataProvider<String> dataProvider = new ListDataProvider<String>();
		for(ServerGroupRecord group : serverGroups){
			dataProvider.getList().add(group.getName());
		}
		return new DefaultNodeInfo<String>(dataProvider, new TextCell());
	}
	
	@Override
	public boolean isLeaf(Object value) {
		return false;
	}
	
}
