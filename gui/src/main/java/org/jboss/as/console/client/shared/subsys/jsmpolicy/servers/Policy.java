package org.jboss.as.console.client.shared.subsys.jsmpolicy.servers;

public class Policy {

    String name;
    String file;

    public Policy(String name, String file){
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFile() {
        return file;
    }
    public void setFile(String file) {
        this.file = file;
    }

}
