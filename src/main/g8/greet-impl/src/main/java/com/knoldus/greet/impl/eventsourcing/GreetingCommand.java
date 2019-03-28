package com.knoldus.greet.impl.eventsourcing;

import akka.Done;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.knoldus.greet.impl.util.Constants;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

public interface GreetingCommand extends Jsonable {
    
    @SuppressWarnings(Constants.SerialVersionUID)
    @Value
    @JsonDeserialize
    final class UseGreetingMessage implements GreetingCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {
        public final String message;
        
        @JsonCreator
        public UseGreetingMessage(String message) {
            this.message = Preconditions.checkNotNull(message, Constants.MessageField);
        }
    }
    
    @SuppressWarnings(Constants.SerialVersionUID)
    @Value
    @JsonDeserialize
    final class Greeting implements GreetingCommand, PersistentEntity.ReplyType<String> {
        
        public final String name;
        
        @JsonCreator
        public Greeting(String name) {
            this.name = Preconditions.checkNotNull(name, Constants.NameField);
        }
    }
    
}
