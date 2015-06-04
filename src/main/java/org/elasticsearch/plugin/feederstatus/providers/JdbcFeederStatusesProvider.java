package org.elasticsearch.plugin.feederstatus.providers;


import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.common.collect.UnmodifiableIterator;
import org.elasticsearch.plugin.feederstatus.model.Status;
import org.elasticsearch.plugin.feederstatus.model.StatusType;
import org.elasticsearch.plugin.feederstatus.model.Statuses;
import org.xbib.elasticsearch.plugin.jdbc.state.RiverState;
import org.xbib.elasticsearch.plugin.jdbc.state.RiverStatesMetaData;

import java.util.ArrayList;

public class JdbcFeederStatusesProvider implements StatusesProvider {

    private Client client;

    public JdbcFeederStatusesProvider(Client client) {
        this.client = client;
    }

    @Override
    public Statuses getStatuses() {
        ClusterState clusterState = client.admin().cluster().prepareState().execute().actionGet().getState();
        RiverStatesMetaData riverStatesMetaData = clusterState.metaData().custom(RiverStatesMetaData.TYPE);
        UnmodifiableIterator it = riverStatesMetaData.getRiverStates().iterator();

        Statuses statusList = new Statuses();
        statusList.setStatuses(new ArrayList<Status>());

        while(it.hasNext()) {
            RiverState rs = (RiverState) it.next();

            Status status = new Status();
            status.setName(rs.getName());
            status.setStatus(StatusType.IDLE);
            if(rs.getLastActiveEnd() != null) {
                status.setLastRunDate(rs.getLastActiveEnd().toString());
            }

            statusList.getStatuses().add(status);
        }

        return statusList;
    }


    @Override
    public boolean supports(Feeder feeder) {
        return Feeder.JDBC_FEEDER.equals(feeder);
    }
}
