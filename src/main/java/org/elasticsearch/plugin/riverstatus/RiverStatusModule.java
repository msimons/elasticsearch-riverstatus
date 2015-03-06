package org.elasticsearch.plugin.riverstatus;

import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.plugin.riverstatus.rest.RiverStatusRequestHandler;


public class RiverStatusModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RiverStatusRequestHandler.class).asEagerSingleton();
    }
}
