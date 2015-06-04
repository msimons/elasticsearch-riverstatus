package org.elasticsearch.plugin.feederstatus.rest;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugin.feederstatus.FeederStatusPlugin;
import org.elasticsearch.plugin.feederstatus.model.Statuses;
import org.elasticsearch.plugin.feederstatus.providers.StatusesProvider;
import org.elasticsearch.plugin.feederstatus.providers.StatusesProviderFactory;
import org.elasticsearch.rest.*;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestStatus.NOT_FOUND;
import static org.elasticsearch.rest.RestStatus.OK;


public class StatusRequestHandler extends BaseRestHandler {

    @Inject
    public StatusRequestHandler(Settings settings, RestController controller, Client client) {
        super(settings,controller,client);

        controller.registerHandler(GET, FeederStatusPlugin.PATH_NAME + "/info", this);
    }

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception {
        StatusesProviderFactory f = new StatusesProviderFactory(client);
        StatusesProvider provider = f.getProvider();

        RestResponse response;
        if(provider == null) {
            response = new BytesRestResponse(NOT_FOUND, "application/json", "Provider not found!");
        } else {
            Statuses riverStatusResponse = provider.getStatuses();
            response = new BytesRestResponse(OK, "application/json", riverStatusResponse.toJson());

        }

        response.addHeader("Access-Control-Allow-Methods", "GET");

        channel.sendResponse(response);

    }
}
