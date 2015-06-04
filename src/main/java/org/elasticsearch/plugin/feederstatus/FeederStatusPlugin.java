package org.elasticsearch.plugin.feederstatus;


import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.plugins.AbstractPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FeederStatusPlugin extends AbstractPlugin {

    public final static String PATH_NAME = "_feederstatus";

    @Inject
    public FeederStatusPlugin() {
    }

    @Override
    public String name() {
        return "feederstatus";
    }

    @Override
    public String description() {
        return "Feeder Status";
    }

    @Override
    public Collection<Class<? extends Module>> modules() {
        List<Class> modules = new ArrayList<>();
        modules.add(FeederStatusModule.class);
        return (Collection) modules;
    }
}
