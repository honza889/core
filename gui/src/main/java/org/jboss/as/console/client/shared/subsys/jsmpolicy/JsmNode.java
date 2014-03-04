package org.jboss.as.console.client.shared.subsys.jsmpolicy;

import java.util.ArrayList;
import java.util.List;

public class JsmNode {
	String name;
	String policy = "init";
	List<JsmNode> nodes = new ArrayList<JsmNode>();
	JsmPresenter presenter;

	public JsmNode(String name, JsmPresenter presenter){
		this.name = name;
		this.presenter = presenter;
	}

	public String getName(){
		return name;
	}

	public List<JsmNode> getNodes(){
		return nodes;
	}

	public String getPolicy(){
	    if(nodes.isEmpty()){ // server (empty groups are not here)
	        return this.policy;
	    }else{ // group
	        JsmNode first = nodes.get(0);

	        for(JsmNode subnode : nodes){
	            if(!subnode.policy.equals(first.policy)){
	                return "";
	            }
            }
	        return first.policy;
	    }
	}

    public void initPolicy(String policy){
        this.policy = policy;
    }

	public void setPolicy(String policy){
	    if(policy!=null && policy.length()==0) policy=null;
	    this.policy = policy;
	    if(nodes.isEmpty()){ // server
	        presenter.setServerPolicy(name, policy);
	    }else{ // group
	        for(JsmNode subnode : nodes){
	            subnode.setPolicy(policy);
	        }
	    }
	}

}