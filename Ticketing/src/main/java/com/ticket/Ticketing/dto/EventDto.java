package com.ticket.Ticketing.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class EventDto {
    private String id;
    private String managerId;
    private Integer row;
    private Integer col;
    private String eventName;
    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;
    private Boolean eventStatus;
}
