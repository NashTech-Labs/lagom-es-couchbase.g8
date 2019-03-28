package com.knoldus.greet.impl.eventsourcing;

import akka.Done;
import com.knoldus.greet.impl.util.Constants;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GreetingEntity extends PersistentEntity<GreetingCommand, GreetingEvent, GreetingState> {
    
    private GreetingState defaultState = new GreetingState(Constants.InitialGreeting, Constants.InitialTimestamp);
    
    @Override
    public Behavior initialBehavior(Optional<GreetingState> snapshotState) {
        BehaviorBuilder b = newBehaviorBuilder(snapshotState.orElse(defaultState));
        
        b.setCommandHandler(GreetingCommand.UseGreetingMessage.class, (cmd, ctx) -> {
            List<GreetingEvent.GreetingMessageChanged> events = new ArrayList<>();
            events.add(new GreetingEvent.GreetingMessageChanged(entityId(), cmd.message));
            return ctx.thenPersistAll(events,
                    () -> ctx.reply(Done.getInstance()));
        });
        
        b.setReadOnlyCommandHandler(GreetingCommand.Greeting.class,
                (cmd, ctx) -> ctx.reply(state().getMessage() + ", " + cmd.name + Constants.Exclamation));
        
        b.setEventHandler(GreetingEvent.GreetingMessageChanged.class,
                evt -> new GreetingState(evt.message, getCurrentTime()));
        
        return b.build();
    }
    
    private String getCurrentTime() {
        return new java.util.Date().toString();
    }
    
}
