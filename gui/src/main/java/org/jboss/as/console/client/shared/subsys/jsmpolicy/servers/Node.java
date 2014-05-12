package org.jboss.as.console.client.shared.subsys.jsmpolicy.servers;

import java.util.ArrayList;
import java.util.List;

public class Node {
	String name;
	String policy = "init";
	List<Node> nodes = new ArrayList<Node>();
	ServersPresenter presenter;

	public Node(String name, ServersPresenter presenter){
		this.name = name;
		this.presenter = presenter;
	}

	public String getName(){
		return name;
	}

	public List<Node> getNodes(){
		return nodes;
	}

	public String getPolicy(){
	    if(nodes.isEmpty()){ // server (empty groups are not here)
	        return this.policy;
	    }else{ // group
	        Node first = nodes.get(0);

	        for(Node subnode : nodes){
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
	        for(Node subnode : nodes){
	            subnode.setPolicy(policy);
	        }
	    }
	}

}