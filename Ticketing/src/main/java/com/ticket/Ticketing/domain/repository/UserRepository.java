package com.ticket.Ticketing.domain.repository;

import com.ticket.Ticketing.domain.document.UserDocument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// CRUD

@Repository

public interface UserRepository extends CrudRepository<UserDocument, String> {

}
