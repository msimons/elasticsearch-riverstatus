package org.elasticsearch.plugin.riverstatus.providers;

import org.elasticsearch.action.admin.cluster.node.info.NodeInfo;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequest;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.action.admin.cluster.node.info.PluginInfo;
import org.elasticsearch.client.Client;

import java.util.*;


public class RiverStatusesProviderFactory {

    private Client client;
    private List<RiverStatusesProvider> providers = new ArrayList<>();

    public RiverStatusesProviderFactory(Client client) {
        this.client = client;
        providers.add(new RiverStatusesProviderImpl(client));
    }

    public RiverStatusesProvider getProvider(){
        Set<RiverStatusesProvider> selected = new HashSet<>();
        for(River river : current()) {
            for(RiverStatusesProvider provider : providers) {
                if(!provider.supports(river)){
                    continue;
                }

                selected.add(provider);
            }
        }

        if(selected.size() == 0) {
            return null;
        }

        if(selected.size() > 1) {
            return new RiverStatusesProviderAggregator(selected);
        }

        return selected.iterator().next();
    }

    private Collection<River> current() {
        Set<River> result = new HashSet<>();
        NodesInfoResponse nodesInfo = client.admin().cluster().nodesInfo(new NodesInfoRequest().clear().plugins(true)).actionGet();
        for(NodeInfo nodeInfo : nodesInfo.getNodes()) {
            for(PluginInfo pluginInfo : nodeInfo.getPlugins().getInfos()){
                    River river = River.valueByName(pluginInfo.getName());
                    if(river != null){
                        result.add(river);
                    }
            }
        }
        return result;
    }
}
