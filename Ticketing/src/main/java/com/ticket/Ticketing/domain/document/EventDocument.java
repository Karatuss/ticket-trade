package com.ticket.Ticketing.domain.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@Document
public class EventDocument {
    @Id
    @GeneratedValue(strategy = GenerationStrategy.USE_ATTRIBUTES)
    final String id;

    @Field
    String managerId;

    @Field
    Integer row;

    @Field
    Integer col;

    @Field
    String eventName;

    @Field
    LocalDateTime eventStart;

    @Field
    LocalDateTime eventEnd;

    @Field
    Boolean eventStatus;
}
