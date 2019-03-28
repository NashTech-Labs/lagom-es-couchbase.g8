package com.knoldus.greet.impl.eventsourcing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.knoldus.greet.impl.util.Constants;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventShards;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

public interface GreetingEvent extends Jsonable, AggregateEvent<GreetingEvent> {
    
    AggregateEventShards<GreetingEvent> TAG = AggregateEventTag.sharded(GreetingEvent.class, 4);
    
    @SuppressWarnings(Constants.SerialVersionUID)
    @Value
    @JsonDeserialize
    final class GreetingMessageChanged implements GreetingEvent {
        
        public final String name;
        public final String message;
        
        @JsonCreator
        public GreetingMessageChanged(String name, String message) {
            this.name = Preconditions.checkNotNull(name, Constants.NameField);
            this.message = Preconditions.checkNotNull(message, Constants.MessageField);
        }
    }
    
    @Override
    default AggregateEventTagger<GreetingEvent> aggregateTag() {
        return TAG;
    }
    
}
