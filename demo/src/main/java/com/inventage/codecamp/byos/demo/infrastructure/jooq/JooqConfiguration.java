package com.inventage.codecamp.byos.demo.infrastructure.jooq;

import byos.PrettyPrinter;
import io.quarkiverse.jooq.runtime.JooqCustomContext;
import org.jboss.logging.Logger;
import org.jooq.Configuration;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.impl.DefaultExecuteListenerProvider;

public class JooqConfiguration implements JooqCustomContext {

    private static final Logger LOGGER = Logger.getLogger(JooqConfiguration.class);

    @Override
    public void apply(Configuration configuration) {
        JooqCustomContext.super.apply(configuration);
        configuration.set(new DefaultExecuteListenerProvider(new PrettyPrinter()));
        LOGGER.debugf("apply");
    }

}
