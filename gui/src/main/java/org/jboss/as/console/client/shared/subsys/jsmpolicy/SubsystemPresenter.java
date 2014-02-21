package org.picketbox.jsmpolicy.console.client.jsmpolicy;

import org.jboss.as.console.client.shared.subsys.RevealStrategy;
import org.jboss.as.console.client.shared.viewframework.FrameworkView;
import org.jboss.as.console.spi.RuntimeExtension;
import org.jboss.as.console.spi.SubsystemExtension;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.Proxy;
import org.jboss.as.console.spi.AccessControl;

public class SubsystemPresenter extends Presenter<SubsystemPresenter.MyView, SubsystemPresenter.MyProxy> {
	
	private RevealStrategy revealStrategy;
	
	@ProxyCodeSplit
	@NameToken("jsmpolicy")
	@AccessControl(resources = {
            "{selected.profile}/subsystem=jsmpolicy"
    })
	@SubsystemExtension(name = "Server policies", group = "JSM POLICY", key = "jsmpolicy")
	@RuntimeExtension(name="JSM POLICY", key="jsmpolicy")
	public interface MyProxy extends Proxy<SubsystemPresenter>, Place {}
	public interface MyView extends View, FrameworkView {}
	
	@Inject
	public SubsystemPresenter(EventBus eventBus, MyView view, MyProxy proxy, RevealStrategy revealStrategy) {
		super(eventBus, view, proxy);
		this.revealStrategy = revealStrategy;
	}
	
	protected void revealInParent() {
		revealStrategy.revealInParent(this);
	}
	
	protected void onReset() {
        super.onReset();
        getView().initialLoad();
    }
	
}
