package org.jboss.as.console.client.shared.subsys.jsmpolicy.policies;

import org.jboss.as.console.client.widgets.forms.Address;
import org.jboss.as.console.client.widgets.forms.Binding;

@Address("/subsystem=jsmpolicy/policy={0}")
public interface PolicyEntity {

    @Binding(detypedName="name", key = true)
    String getName();
    void setName(String name);

    @Binding(detypedName = "file")
    String getFile();
    void setFile(String file);

}
