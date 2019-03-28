package com.knoldus.greet.api.models;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public final class UserGreeting {
    String user;
    String message;
}
