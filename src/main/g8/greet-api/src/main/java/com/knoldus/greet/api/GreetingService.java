package com.knoldus.greet.api;

import akka.Done;
import akka.NotUsed;
import com.knoldus.greet.api.models.GreetingMessage;
import com.knoldus.greet.api.models.UserGreeting;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import java.util.List;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

public interface GreetingService extends Service {
    
    ServiceCall<NotUsed, String> greet(String id);
    
    ServiceCall<GreetingMessage, Done> useGreeting(String id);
    
    ServiceCall<NotUsed, List<UserGreeting>> userGreetings();
    
    @Override
    default Descriptor descriptor() {
        return named("greeting").withCalls(
                pathCall("/api/greet/:id", this::greet),
                pathCall("/api/greet/:id", this::useGreeting),
                pathCall("/api/user-greetings", this::userGreetings)
        ).withAutoAcl(true);
    }
}
