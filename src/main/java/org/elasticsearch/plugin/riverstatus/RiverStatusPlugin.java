package org.elasticsearch.plugin.riverstatus;


import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.plugins.AbstractPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RiverStatusPlugin extends AbstractPlugin {

    public final static String PATH_NAME = "_riverstatus";

    @Inject
    public RiverStatusPlugin() {
    }

    @Override
    public String name() {
        return "riverstatus";
    }

    @Override
    public String description() {
        return "River Status";
    }

    @Override
    public Collection<Class<? extends Module>> modules() {
        List<Class> modules = new ArrayList<>();
        modules.add(RiverStatusModule.class);
        return (Collection) modules;
    }
}
