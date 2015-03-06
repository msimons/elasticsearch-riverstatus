package org.elasticsearch.plugin.riverstatus.model;


import com.google.gson.GsonBuilder;
import lombok.Data;

import java.util.List;

@Data
public class RiverStatusResponse {
    private List<RiverStatus> rivers;

    /**
     * Returns a valid JSON representation of the model, according to the Swagger schema.
     */
    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.setPrettyPrinting();
        return gson.create().toJson(this);
    }
}
