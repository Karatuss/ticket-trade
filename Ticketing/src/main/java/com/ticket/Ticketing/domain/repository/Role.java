package com.ticket.Ticketing.domain.repository;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_ADMIN("관리자"), ROLE_MEMBER("사용자");

    private final String description;
    Role(String description){
        this.description = description;
    }
}
