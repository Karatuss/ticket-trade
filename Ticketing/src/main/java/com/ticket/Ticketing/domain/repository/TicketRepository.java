package com.ticket.Ticketing.domain.repository;

import com.ticket.Ticketing.domain.document.TicketDocument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends CrudRepository<TicketDocument, String> {
}
