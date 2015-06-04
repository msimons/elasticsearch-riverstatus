package org.elasticsearch.plugin.feederstatus.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "name")
public class Status {
    private String name;
    private StatusType status;
    private String lastRunDate;
}
