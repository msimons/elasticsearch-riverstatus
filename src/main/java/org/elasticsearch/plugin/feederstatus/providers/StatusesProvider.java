package org.elasticsearch.plugin.feederstatus.providers;

import org.elasticsearch.plugin.feederstatus.model.Statuses;


public interface StatusesProvider {
    Statuses getStatuses();
    boolean supports(Feeder river);
}
