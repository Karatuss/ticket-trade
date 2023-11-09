package com.ticket.Ticketing.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class EventDto {
    private String id;
    private String managerId;
    private Integer seatNum;
    private String eventName;
    private List<String> participants;
}
