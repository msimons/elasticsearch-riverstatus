package org.elasticsearch.plugin.feederstatus;

import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.plugin.feederstatus.rest.StatusRequestHandler;


public class FeederStatusModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(StatusRequestHandler.class).asEagerSingleton();
    }
}
