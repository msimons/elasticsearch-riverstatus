package org.elasticsearch.plugin.riverstatus.rest;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugin.riverstatus.RiverStatusPlugin;
import org.elasticsearch.plugin.riverstatus.model.RiverStatuses;
import org.elasticsearch.plugin.riverstatus.providers.RiverStatusesProvider;
import org.elasticsearch.plugin.riverstatus.providers.RiverStatusesProviderFactory;
import org.elasticsearch.rest.*;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestStatus.NOT_FOUND;
import static org.elasticsearch.rest.RestStatus.OK;


public class RiverStatusRequestHandler extends BaseRestHandler {

    @Inject
    public RiverStatusRequestHandler(Settings settings, RestController controller, Client client) {
        super(settings,controller,client);

        controller.registerHandler(GET, RiverStatusPlugin.PATH_NAME + "/info", this);
    }

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception {
        RiverStatusesProviderFactory f = new RiverStatusesProviderFactory(client);
        RiverStatusesProvider provider = f.getProvider();

        RestResponse response;
        if(provider == null) {
            response = new BytesRestResponse(NOT_FOUND, "application/json", "Provider not found!");
        } else {
            RiverStatuses riverStatusResponse = provider.getRiverStatuses();
            response = new BytesRestResponse(OK, "application/json", riverStatusResponse.toJson());

        }

        response.addHeader("Access-Control-Allow-Methods", "GET");

        channel.sendResponse(response);

    }
}
