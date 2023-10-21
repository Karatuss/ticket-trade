package com.ticket.Ticketing.domain.repository;

import com.ticket.Ticketing.domain.document.UserDocument;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

// CRUD

public interface UserRepository extends CrudRepository<UserDocument, String> {
    Optional<UserDocument> findByIdAndPassword(Object id, Object password);
}
