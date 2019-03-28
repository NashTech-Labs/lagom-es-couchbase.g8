package com.knoldus.greet.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import lombok.Value;

@Value
@JsonDeserialize
public final class GreetingMessage {
    
    String message;
    
    @JsonCreator
    public GreetingMessage(String message) {
        this.message = Preconditions.checkNotNull(message, "message field MUST not be null");
    }
}
