package org.elasticsearch.plugin.riverstatus.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Index {
    private String name;
    private String type;
}
