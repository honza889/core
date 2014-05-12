package org.jboss.as.console.client.shared.subsys.jsmpolicy.servers;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Command;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.TreeViewModel;

public class ServersTreeViewModel implements TreeViewModel {

    Map<String, Node> serverGroups;
    List<Policy> policyPossibleValues;
    ListDataProvider<Node> dataProvider = new ListDataProvider<Node>();
    private static Command finishCmd = null;

    public ServersTreeViewModel(Map<String, Node> serverGroups, List<Policy> policyPossibleValues) {
        this.serverGroups = serverGroups;
        this.policyPossibleValues = policyPossibleValues;
    }

    public <T> NodeInfo<?> getNodeInfo(T value) {

        final ListDataProvider<Node> dataProvider = new ListDataProvider<Node>();

        if (value instanceof Node) {
            for (Node server : ((Node) value).getNodes()) {
                dataProvider.getList().add(server);
            }
        } else {
            setFinish(new Command() {
                public void execute() {
                    if(serverGroups!=null){
                        dataProvider.getList().clear();
                        for (Node group : serverGroups.values()) {
                            dataProvider.getList().add(group);
                        }
                    }
                }
            });
        }

        return new DefaultNodeInfo<Node>(dataProvider, new NodeCell(policyPossibleValues));
    }

    public boolean isLeaf(Object value) {
        return (value instanceof Node) && ((Node) value).getNodes().isEmpty();
    }

    public static void setFinish(Command finishCmd) {
        ServersTreeViewModel.finishCmd = finishCmd;
    }

    public static void runFinish() {
        Command finishCmd = ServersTreeViewModel.finishCmd;
        if (finishCmd != null) finishCmd.execute();
    }

}
