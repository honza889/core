package org.jboss.as.console.client.shared.subsys.jsmpolicy;

import java.util.ArrayList;
import java.util.List;

import org.jboss.as.console.client.Console;

public class JsmNode {
	String name;
	boolean group;
	String policy = "init";
	List<JsmNode> nodes = new ArrayList<JsmNode>();

	public JsmNode(String name, boolean group){
		this.name = name;
		this.group = group;
	}

	public String getName(){
		return name;
	}

	public List<JsmNode> getNodes(){
		return nodes;
	}

	public boolean isGroup(){
		return group;
	}

	public String getPolicy(){
	    if(nodes.isEmpty()){ // server (empty groups are not here)
	        return this.policy;
	    }else{ // group
	        JsmNode first = nodes.get(0);

	        Console.error("firstValue("+first.name+")("+first.policy+")",first.toString());

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

	public void setPolicy(String policy){

	    Console.error(name+".setPolicy("+policy+")", this.toString());

	    this.policy = policy;
	    for(JsmNode subnode : nodes){
	        subnode.setPolicy(policy);
	    }

	}

}