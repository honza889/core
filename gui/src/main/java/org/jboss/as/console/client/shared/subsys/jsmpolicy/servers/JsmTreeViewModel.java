package org.jboss.as.console.client.shared.subsys.jsmpolicy.servers;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Command;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.TreeViewModel;

public class JsmTreeViewModel implements TreeViewModel {

    Map<String, JsmNode> serverGroups;
    List<JsmPolicy> policyPossibleValues;
    ListDataProvider<JsmNode> dataProvider = new ListDataProvider<JsmNode>();
    private static Command finishCmd = null;

    public JsmTreeViewModel(Map<String, JsmNode> serverGroups, List<JsmPolicy> policyPossibleValues) {
        this.serverGroups = serverGroups;
        this.policyPossibleValues = policyPossibleValues;
    }

    public <T> NodeInfo<?> getNodeInfo(T value) {

        final ListDataProvider<JsmNode> dataProvider = new ListDataProvider<JsmNode>();

        if (value instanceof JsmNode) {
            for (JsmNode server : ((JsmNode) value).getNodes()) {
                dataProvider.getList().add(server);
            }
        } else {
            setFinish(new Command() {
                public void execute() {
                    dataProvider.getList().clear();
                    for (JsmNode group : serverGroups.values()) {
                        dataProvider.getList().add(group);
                    }
                }
            });
        }

        return new DefaultNodeInfo<JsmNode>(dataProvider, new JsmNodeCell(policyPossibleValues));
    }

    public boolean isLeaf(Object value) {
        return (value instanceof JsmNode) && ((JsmNode) value).getNodes().isEmpty();
    }

    public static void setFinish(Command finishCmd) {
        JsmTreeViewModel.finishCmd = finishCmd;
    }

    public static void runFinish() {
        if (finishCmd != null)
            finishCmd.execute();
    }

}
