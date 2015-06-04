package org.elasticsearch.plugin.feederstatus.providers;


import org.elasticsearch.plugin.feederstatus.model.Statuses;

import java.util.ArrayList;
import java.util.Collection;

public class StatusesProviderAggregator implements StatusesProvider {

    private Collection<StatusesProvider> providers = new ArrayList<>();

    public StatusesProviderAggregator(Collection<StatusesProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Statuses getStatuses() {
        Statuses response = null;
        for(StatusesProvider provider : providers) {
            Statuses r = provider.getStatuses();
            if(response == null) {
                response = r;
            } else {
                response.getStatuses().addAll(r.getStatuses());
            }
        }
        return null;
    }

    @Override
    public boolean supports(Feeder river) {
        for(StatusesProvider provider : providers) {
            if(provider.supports(river)) {
                return true;
            }
        }
        return false;
    }
}
