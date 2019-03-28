package com.knoldus.greet.impl.eventsourcing;

import akka.Done;
import akka.stream.alpakka.couchbase.javadsl.CouchbaseSession;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.knoldus.greet.impl.util.Constants;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.couchbase.CouchbaseReadSide;
import org.pcollections.PSequence;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static com.knoldus.greet.impl.util.Constants.DOC_ID;

public class GreetingEventProcessor extends ReadSideProcessor<GreetingEvent> {
    
    private CouchbaseReadSide couchbaseReadSide;
    
    @Inject
    public GreetingEventProcessor(CouchbaseReadSide couchbaseReadSide) {
        this.couchbaseReadSide = couchbaseReadSide;
    }
    
    @Override
    public ReadSideHandler<GreetingEvent> buildHandler() {
        return couchbaseReadSide.<GreetingEvent>builder(Constants.ReadSideId)
                .setGlobalPrepare(this::globalPrepare)
                .setEventHandler(GreetingEvent.GreetingMessageChanged.class, this::processGreetingMessageChanged)
                .build();
    }
    
    @Override
    public PSequence<AggregateEventTag<GreetingEvent>> aggregateTags() {
        return GreetingEvent.TAG.allTags();
    }
    
    private CompletionStage<Done> globalPrepare(CouchbaseSession session) {
        return
                session.get(DOC_ID).thenComposeAsync(doc -> {
                    if (doc.isPresent()) {
                        return CompletableFuture.completedFuture(Done.getInstance());
                    }
                    return session.insert(JsonDocument.create(DOC_ID, JsonObject.empty()))
                            .thenApply(ignore -> Done.getInstance());
                });
    }
    
    private CompletionStage<Done> processGreetingMessageChanged(CouchbaseSession session, GreetingEvent.GreetingMessageChanged evt) {
        return session.get(DOC_ID).thenComposeAsync(docOpt -> {
            JsonDocument doc = docOpt.get();
            doc.content().put(evt.name, evt.message);
            return session.upsert(doc);
        }).thenApply(ignore -> Done.getInstance());
    }
}
