package org.elasticsearch.plugin.riverstatus.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "name")
public class RiverStatus {
    private String name;
    private String strategy;
    private String poll;
    private boolean running;
    private Status status;
    private String lastRunDate;
    private Node node;
    private Index index;
}
