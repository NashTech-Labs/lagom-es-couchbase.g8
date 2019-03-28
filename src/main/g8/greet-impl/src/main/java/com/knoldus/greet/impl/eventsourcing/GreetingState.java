package com.knoldus.greet.impl.eventsourcing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.knoldus.greet.impl.util.Constants;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.Value;

@SuppressWarnings(Constants.SerialVersionUID)
@Value
@JsonDeserialize
public final class GreetingState implements CompressedJsonable {
    
    public final String message;
    public final String timestamp;
    
    @JsonCreator
    GreetingState(String message, String timestamp) {
        this.message = Preconditions.checkNotNull(message, "message" + Constants.ErrorMessage);
        this.timestamp = Preconditions.checkNotNull(timestamp, "timestamp" + Constants.ErrorMessage);
    }
}
