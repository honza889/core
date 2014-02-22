package org.jboss.as.console.client.shared.subsys.jsmpolicy;

import java.util.List;
import java.util.Map;

import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.domain.model.SimpleCallback;
import org.jboss.as.console.client.shared.subsys.Baseadress;
import org.jboss.as.console.client.shared.subsys.RevealStrategy;
import org.jboss.as.console.client.shared.subsys.jpa.model.JpaSubsystem;
import org.jboss.as.console.client.shared.subsys.mail.MailPresenter;
import org.jboss.as.console.client.shared.subsys.mail.MailSession;
import org.jboss.as.console.client.shared.viewframework.FrameworkView;
import org.jboss.as.console.spi.RuntimeExtension;
import org.jboss.as.console.spi.SubsystemExtension;
import org.jboss.as.console.spi.AccessControl;
import org.jboss.dmr.client.ModelNode;
import org.jboss.dmr.client.ModelType;
import org.jboss.dmr.client.dispatch.impl.DMRAction;
import org.jboss.dmr.client.dispatch.impl.DMRResponse;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.Proxy;

public class JsmPresenter extends Presenter<JsmPresenter.MyView, JsmPresenter.MyProxy> {
	
	private RevealStrategy revealStrategy;
	
	@ProxyCodeSplit
	@NameToken("jsmpolicy")
	@AccessControl(resources = {
            "{selected.profile}/subsystem=jsmpolicy"
    })
	@SubsystemExtension(name = "JSM Policies", group = "JSM Policy", key = "jsmpolicy")
	//@RuntimeExtension(name="JSM POLICY", key="jsmpolicy")
	public interface MyProxy extends Proxy<JsmPresenter>, Place {}
	public interface MyView extends View, FrameworkView {
		void setPresenter(MailPresenter presenter);
        void updateFrom(List<MailSession> list);
        void setSelectedSession(String selectedSession);
	}
	
	@Inject
	public JsmPresenter(EventBus eventBus, MyView view, MyProxy proxy, RevealStrategy revealStrategy) {
		super(eventBus, view, proxy);
		this.revealStrategy = revealStrategy;
	}
	
	protected void onReset() {
        super.onReset();
        getView().initialLoad();
    }
	
	protected void revealInParent() {
		revealStrategy.revealInParent(this);
	}

	public void onSave(JsmServer editedEntity, Map<String, Object> changeset) {
		/*
        ModelNode operation = adapter.fromChangeset(changeset, beanMetaData.getAddress().asResource(Baseadress.get()));

        if(changeset.containsKey("defaultDataSource") && changeset.get("defaultDataSource").equals(""))
        {
            changeset.remove("defaultDataSource");
            operation.get("default-datasource").set(ModelType.UNDEFINED);
        }

        // TODO: https://issues.jboss.org/browse/AS7-3596
        dispatcher.execute(new DMRAction(operation), new SimpleCallback<DMRResponse>() {
            @Override
            public void onSuccess(DMRResponse result) {
                ModelNode response  = result.get();

                if(response.isFailure())
                {
                    Console.error(Console.MESSAGES.modificationFailed("JPA Subsystem"), response.getFailureDescription());
                }
                else
                {
                    Console.info(Console.MESSAGES.modified("JPA Subsystem"));
                }

                loadSubsystem();
            }
        });
        */
    }
}
