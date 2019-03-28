package com.knoldus.greet.impl;

import akka.Done;
import akka.NotUsed;
import com.knoldus.greet.api.GreetingService;
import com.knoldus.greet.api.models.GreetingMessage;
import com.knoldus.greet.api.models.UserGreeting;
import com.knoldus.greet.impl.dao.CouchbaseRepository;
import com.knoldus.greet.impl.eventsourcing.GreetingCommand;
import com.knoldus.greet.impl.eventsourcing.GreetingEntity;
import com.knoldus.greet.impl.eventsourcing.GreetingEventProcessor;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;

import javax.inject.Inject;
import java.util.List;

/**
 * Implementation of the GreetService.
 */
public class GreetingServiceImpl implements GreetingService {
    private final PersistentEntityRegistry persistentEntityRegistry;
    private CouchbaseRepository couchbaseRepository;
    
    @Inject
    public GreetingServiceImpl(PersistentEntityRegistry persistentEntityRegistry,
                               CouchbaseRepository couchbaseRepository,
                               ReadSide readSide) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        persistentEntityRegistry.register(GreetingEntity.class);
        this.couchbaseRepository = couchbaseRepository;
        readSide.register(GreetingEventProcessor.class);
    }
    
    @Override
    public ServiceCall<NotUsed, String> greet(String id) {
        return request -> ref(id).ask(new GreetingCommand.Greeting(id));
    }
    
    @Override
    public ServiceCall<GreetingMessage, Done> useGreeting(String id) {
        return request -> ref(id).ask(new GreetingCommand.UseGreetingMessage(request.getMessage()));
    }
    
    @Override
    public ServiceCall<NotUsed, List<UserGreeting>> userGreetings() {
        return request -> couchbaseRepository.listUserGreetings();
    }
    
    private PersistentEntityRef<GreetingCommand> ref(String id) {
        return persistentEntityRegistry.refFor(GreetingEntity.class, id);
    }
}
