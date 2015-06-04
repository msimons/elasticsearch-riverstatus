package org.elasticsearch.plugin.feederstatus.providers;


import lombok.Getter;

public enum Feeder {
    JDBC_FEEDER("JDBC plugin");

    private @Getter String name;

    private Feeder(String name) {
        this.name = name;
    }

    public static Feeder valueByName(String name) {
        for(Feeder feeder : Feeder.values()) {
            if(feeder.getName().equals(name)) {
                return feeder;
            }
        }

       return null;
    }

}
