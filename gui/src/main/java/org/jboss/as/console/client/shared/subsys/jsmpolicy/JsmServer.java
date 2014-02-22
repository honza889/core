package org.jboss.as.console.client.shared.subsys.jsmpolicy;

import org.jboss.as.console.client.shared.viewframework.NamedEntity;
import org.jboss.as.console.client.widgets.forms.Address;
import org.jboss.as.console.client.widgets.forms.Binding;

@Address("/subsystem=jsmpolicy/server={0}")
public interface JsmServer extends NamedEntity {
	
	@Binding(detypedName="name", key = true)
	String getName();
	void setName(String alias);
	
	@Binding(detypedName="policy")
	String getPolicy();
	void setPolicy(String policy);    
	
}