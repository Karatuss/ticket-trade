package com.ticket.Ticketing.dto;

import lombok.*;
import org.springframework.data.couchbase.core.mapping.Field;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class SeatDto {
    private String id;
    private String seatNum;
    private Boolean sold;
}
