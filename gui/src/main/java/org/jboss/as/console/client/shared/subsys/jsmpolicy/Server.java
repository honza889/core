package org.picketbox.jsmpolicy.console.client.jsmpolicy;

import org.jboss.as.console.client.shared.viewframework.NamedEntity;
import org.jboss.as.console.client.widgets.forms.Address;
import org.jboss.as.console.client.widgets.forms.Binding;

@Address("/subsystem=jsmpolicy/server={0}")
public interface Server extends NamedEntity {
	
	@Binding(detypedName="alias", key = true)
	String getName();
	void setName(String alias);
	
	@Binding(detypedName="policy")
	String getPolicy();
	void setPolicy(String policy);    
	
}