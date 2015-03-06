package org.elasticsearch.plugin.riverstatus.providers;


import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.joda.time.DateTime;
import org.elasticsearch.common.joda.time.format.DateTimeFormatter;
import org.elasticsearch.common.joda.time.format.ISODateTimeFormat;
import org.elasticsearch.common.lang3.StringUtils;
import org.elasticsearch.index.query.BaseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.plugin.riverstatus.model.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;

import java.util.*;

public class RiverStatusesProviderImpl implements RiverStatusesProvider {

    private static final int MAX_SIZE = 1000;
    private Client client;

    public RiverStatusesProviderImpl(Client client) {
        this.client = client;
    }

    @Override
    public RiverStatuses getRiverStatuses() {

        RiverStatuses status = new RiverStatuses();
        List<RiverStatus> riverStatus = internalStatus();
        status.setRivers(riverStatus);

        return status;
    }

    public List<RiverStatus> internalStatus() {
        BaseQueryBuilder query = QueryBuilders.idsQuery().ids("_runstatus");
        SearchRequestBuilder builder = client.prepareSearch("_river").setSearchType(SearchType.QUERY_THEN_FETCH).setSize(MAX_SIZE).addFields("_type","jdbc.running", "jdbc.lastUpdate").setQuery(query);
        SearchResponse searchResponse = builder.execute().actionGet();

        Map<String,RiverStatus> map = new HashMap<>();
        for(SearchHit hit : searchResponse.getHits()) {
            String name = hit.getType();
            SearchHitField runningField = hit.field("hit.field");
            SearchHitField lastRunDateField = hit.field("jdbc.lastUpdate");
            boolean running =  (runningField != null ? (Boolean) runningField.value() : false);
            String lastRunDate = (lastRunDateField != null ? (String) lastRunDateField.value() : null);

            RiverStatus status = new RiverStatus();
            status.setName(name);
            status.setRunning(running);

            status.setLastRunDate(lastRunDate);
            map.put(status.getName(), status);
        }

        query = QueryBuilders.idsQuery().ids("_status");
        builder = client.prepareSearch("_river").setSearchType(SearchType.QUERY_THEN_FETCH).setSize(MAX_SIZE).addFields("_type","node.name", "node.transport_address").setQuery(query);
        searchResponse = builder.execute().actionGet();

        for(SearchHit hit : searchResponse.getHits()) {
            String name = hit.getType();
            RiverStatus river = map.get(name);

            if(river == null) {
                continue;
            }

            String nodeName  = hit.field("node.name").value();
            String nodeAddress  = hit.field("node.transport_address").value();

            river.setNode(new Node(nodeName,nodeAddress));
        }

        query = QueryBuilders.idsQuery().ids("_meta");
        builder = client.prepareSearch("_river").setSearchType(SearchType.QUERY_THEN_FETCH).setSize(MAX_SIZE).addFields("jdbc.strategy","jdbc.poll","index.index","index.type").setQuery(query);

        searchResponse = builder.execute().actionGet();

        for(SearchHit hit  : searchResponse.getHits()) {
            String name = hit.getType();
            if(map.containsKey(name)) {
                RiverStatus status = map.get(name);
                status.setStrategy((String) hit.field("jdbc.strategy").getValue());

                SearchHitField jdbcPoll = hit.field("jdbc.poll");
                if(jdbcPoll != null) {
                    status.setPoll((String) jdbcPoll.getValue());
                }
                status.setStatus(getStatus(status.getLastRunDate(), status.getPoll(), status.isRunning()));

                SearchHitField indexName = hit.field("index.index");
                SearchHitField indexType = hit.field("index.type");

                status.setIndex(new Index((String)indexName.getValue(),(String)indexType.getValue()));
            }
        }

        return new ArrayList<>(map.values());
    }

    public Status getStatus(String lastRunDate, String poll, boolean running) {

        if(StringUtils.isBlank(lastRunDate)) {
            if(running) {
                return Status.RUNNING;
            } else {
                return Status.IDLE;
            }
        }

        if(running) {
            if(isBehind(lastRunDate,poll)) {
                return Status.DELAYED;
            } else {
                return Status.RUNNING;
            }
        } else {
            if(StringUtils.isBlank(poll)) {
                // oneshot
                return Status.IDLE;
            } else {
                if(isBehind(lastRunDate,poll)) {
                    return Status.BROKEN;
                } else {
                    return Status.IDLE;
                }
            }
        }
    }

    private boolean isBehind(String lastRunDate, String poll)  {
        DateTimeFormatter formatter = ISODateTimeFormat.dateTimeNoMillis();
        DateTime dt = formatter.parseDateTime(StringUtils.substringBeforeLast(lastRunDate, ".")+ "Z");
        Date date = dt.toDate();

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        // 5 minutes delay accepted
        c.add(Calendar.MINUTE,5);

        if(poll == null) {
            return false;
        }

        if(StringUtils.endsWith(poll.toLowerCase(),"h")) {
            c.add(Calendar.HOUR,Integer.parseInt(StringUtils.substringBeforeLast(poll,"h")));
        } else if(StringUtils.endsWith(poll.toLowerCase(),"m")) {
            c.add(Calendar.MINUTE,Integer.parseInt(StringUtils.substringBeforeLast(poll,"m")));
        }

        Date riverTime = c.getTime();
        Date currentDate = new Date(System.currentTimeMillis());

        if(riverTime.before(currentDate)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean supports(River river) {
        return river.equals(River.JDBC_RIVER);
    }
}
