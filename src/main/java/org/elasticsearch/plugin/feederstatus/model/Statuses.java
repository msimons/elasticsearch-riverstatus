package org.elasticsearch.plugin.feederstatus.model;


import com.google.gson.GsonBuilder;
import lombok.Data;

import java.util.List;

@Data
public class Statuses {
    private List<Status> statuses;

    /**
     * Returns a valid JSON representation of the model, according to the Swagger schema.
     */
    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.setPrettyPrinting();
        return gson.create().toJson(this);
    }
}
