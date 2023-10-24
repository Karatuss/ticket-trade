package com.ticket.Ticketing.dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SeatDto {
    private String id;
    private Boolean sold;

}
