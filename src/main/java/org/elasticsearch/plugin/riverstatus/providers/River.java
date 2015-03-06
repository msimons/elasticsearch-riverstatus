package org.elasticsearch.plugin.riverstatus.providers;


import lombok.Getter;

public enum River {
    JDBC_RIVER("jdbc-river");

    private @Getter String name;

    private River(String name) {
        this.name = name;
    }

    public static  River valueByName(String name) {
        for(River river : River.values()) {
            if(river.getName().equals(name)) {
                return river;
            }
        }

       return null;
    }

}
