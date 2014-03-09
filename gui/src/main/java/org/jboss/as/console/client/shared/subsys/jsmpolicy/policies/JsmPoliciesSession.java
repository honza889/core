package org.jboss.as.console.client.shared.subsys.jsmpolicy.policies;

/**
 * @author Heiko Braun
 * @date 11/28/11
 */

import org.jboss.as.console.client.widgets.forms.Address;
import org.jboss.as.console.client.widgets.forms.Binding;

@Address("/subsystem=mail/mail-session={0}")
public interface JsmPoliciesSession {

    @Binding(skip = true)
    String getName();
    void setName(String name);

    @Binding(detypedName = "jndi-name")
    String getJndiName();
    void setJndiName(String jndiName);

    String getFrom();
    void setFrom(String jndiName);

    boolean isDebug();
    void setDebug(boolean debug);

    @Binding(skip = true)
    MailServer2Definition getSmtpServer();
    void setSmtpServer(MailServer2Definition server);

    @Binding(skip=true)
    MailServer2Definition getImapServer();
    void setImapServer(MailServer2Definition server);

    @Binding(skip=true)
    MailServer2Definition getPopServer();
    void setPopServer(MailServer2Definition server);

}
