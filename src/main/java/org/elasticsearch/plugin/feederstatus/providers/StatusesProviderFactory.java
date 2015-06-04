package org.elasticsearch.plugin.feederstatus.providers;

import org.elasticsearch.action.admin.cluster.node.info.NodeInfo;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequest;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.action.admin.cluster.node.info.PluginInfo;
import org.elasticsearch.client.Client;

import java.util.*;


public class StatusesProviderFactory {

    private Client client;
    private List<StatusesProvider> providers = new ArrayList<>();

    public StatusesProviderFactory(Client client) {
        this.client = client;
        providers.add(new JdbcFeederStatusesProvider(client));
    }

    public StatusesProvider getProvider(){
        Set<StatusesProvider> selected = new HashSet<>();
        for(Feeder river : current()) {
            for(StatusesProvider provider : providers) {
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
            return new StatusesProviderAggregator(selected);
        }

        return selected.iterator().next();
    }

    private Collection<Feeder> current() {
        Set<Feeder> result = new HashSet<>();
        NodesInfoResponse nodesInfo = client.admin().cluster().nodesInfo(new NodesInfoRequest().clear().plugins(true)).actionGet();
        for(NodeInfo nodeInfo : nodesInfo.getNodes()) {
            for(PluginInfo pluginInfo : nodeInfo.getPlugins().getInfos()){
                    Feeder river = Feeder.valueByName(pluginInfo.getName());
                    if(river == null) {
                        river = Feeder.valueByName(pluginInfo.getDescription());
                    }
                    if(river != null){
                        result.add(river);
                    }
            }
        }
        return result;
    }
}
