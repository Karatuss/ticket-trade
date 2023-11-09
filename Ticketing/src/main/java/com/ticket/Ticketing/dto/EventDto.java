package com.ticket.Ticketing.dto;

import lombok.*;

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
}
