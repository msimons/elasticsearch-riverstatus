package org.elasticsearch.plugin.riverstatus.providers;

import org.elasticsearch.plugin.riverstatus.model.RiverStatuses;


public interface RiverStatusesProvider {
    RiverStatuses getRiverStatuses();
    boolean supports(River river);
}
