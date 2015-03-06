package org.elasticsearch.plugin.riverstatus.providers;


import org.elasticsearch.plugin.riverstatus.model.RiverStatuses;

import java.util.ArrayList;
import java.util.Collection;

public class RiverStatusesProviderAggregator implements RiverStatusesProvider {

    private Collection<RiverStatusesProvider> providers = new ArrayList<>();

    public RiverStatusesProviderAggregator(Collection<RiverStatusesProvider> providers) {
        this.providers = providers;
    }

    @Override
    public RiverStatuses getRiverStatuses() {
        RiverStatuses response = null;
        for(RiverStatusesProvider provider : providers) {
            RiverStatuses r = provider.getRiverStatuses();
            if(response == null) {
                response = r;
            } else {
                response.getRivers().addAll(r.getRivers());
            }
        }
        return null;
    }

    @Override
    public boolean supports(River river) {
        for(RiverStatusesProvider provider : providers) {
            if(provider.supports(river)) {
                return true;
            }
        }
        return false;
    }
}
