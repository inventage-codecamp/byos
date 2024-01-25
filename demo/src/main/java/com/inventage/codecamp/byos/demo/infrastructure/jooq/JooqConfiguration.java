package com.inventage.codecamp.byos.demo.infrastructure.jooq;

import io.quarkiverse.jooq.runtime.JooqCustomContext;
import org.jboss.logging.Logger;
import org.jooq.Configuration;

public class JooqConfiguration implements JooqCustomContext {

    private static final Logger LOGGER = Logger.getLogger(JooqConfiguration.class);

    @Override
    public void apply(Configuration configuration) {
        JooqCustomContext.super.apply(configuration);
        LOGGER.debugf("apply");
    }

}
