package com.ticket.Ticketing.domain.repository;

import com.ticket.Ticketing.domain.document.SeatDocument;
import org.springframework.data.repository.CrudRepository;

public interface SeatRepository extends CrudRepository<SeatDocument, String> {
}
