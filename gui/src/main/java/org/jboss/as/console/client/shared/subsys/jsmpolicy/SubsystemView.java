package org.picketbox.jsmpolicy.console.client.jsmpolicy;

import java.util.EnumSet;

import org.jboss.dmr.client.dispatch.DispatchAsync;
import org.jboss.as.console.client.shared.viewframework.AbstractEntityView;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridge;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridgeImpl;
import org.jboss.as.console.client.shared.viewframework.FrameworkButton;
import org.jboss.as.console.client.widgets.forms.ApplicationMetaData;
import org.jboss.ballroom.client.widgets.forms.FormAdapter;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;

import com.google.inject.Inject;

public class SubsystemView extends AbstractEntityView<Server> implements SubsystemPresenter.MyView{
	
	private EntityToDmrBridgeImpl<Server> bridge;
	
	@Inject
    public SubsystemView(ApplicationMetaData propertyMetaData, DispatchAsync dispatchAsync) {
        super(Server.class, propertyMetaData, EnumSet.of(FrameworkButton.ADD, FrameworkButton.REMOVE));
        bridge = new EntityToDmrBridgeImpl<Server>(propertyMetaData, Server.class, this, dispatchAsync);
    }
	
	public EntityToDmrBridge<Server> getEntityBridge() {
		return bridge;
	}
	
	protected DefaultCellTable<Server> makeEntityTable() {
		DefaultCellTable<Server> table = new DefaultCellTable<Server>(5);
        return table;
	}
	
	protected FormAdapter<Server> makeAddEntityForm() {
		return null;
	}
	
	protected String getEntityDisplayName() {
		return "Server (display name)";
	}
}
