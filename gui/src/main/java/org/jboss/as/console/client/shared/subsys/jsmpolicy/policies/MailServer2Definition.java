package org.jboss.as.console.client.shared.subsys.jsmpolicy.policies;

import org.jboss.as.console.client.widgets.forms.Binding;

/**
 * @author Heiko Braun
 * @date 2/14/12
 */
public interface MailServer2Definition {

    @Binding(detypedName = "outbound-socket-binding-ref")
    String getSocketBinding();
    void setSocketBinding(String name);

    @Binding(skip=true)
    ServerType getType();
    void setType(ServerType type);

    @Binding(detypedName = "username")
    String getUsername();
    void setUsername(String name);

    @Binding(detypedName = "password")
    String getPassword();
    void setPassword(String password);

    @Binding(detypedName = "ssl")
    boolean isSsl();
    void setSsl(boolean b);
}
