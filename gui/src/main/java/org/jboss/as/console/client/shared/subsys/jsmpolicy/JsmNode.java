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
	                return "dif";
	            }
            }
	        return first.policy;
	    }
	}

	public static List<String> getPolicyPossibleValues(){
	    List<String> options = new ArrayList<String>();
	    options.add("");
	    options.add("policy1");
        options.add("policy2");
        options.add("policy3");
        options.add("policy4");
        options.add("policy5");
	    return options;
	}

    public void initPolicy(String policy){
        this.policy = policy;
    }

	public void setPolicy(String policy){
	    this.policy = policy;
	    if(nodes.isEmpty()){ // server
	        presenter.setServerPolicy(name, policy);
	    }else{ // group
	        // TODO: if policy!="" ?
	        for(JsmNode subnode : nodes){
	            subnode.setPolicy(policy);
	        }
	    }
	}

}