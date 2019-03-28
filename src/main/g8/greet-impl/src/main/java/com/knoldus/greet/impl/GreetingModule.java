package com.knoldus.greet.impl;

import com.google.inject.AbstractModule;
import com.knoldus.greet.api.GreetingService;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

/**
 * The module that binds the GreetService so that it can be served.
 */
public class GreetingModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(GreetingService.class, GreetingServiceImpl.class);
    }
}
