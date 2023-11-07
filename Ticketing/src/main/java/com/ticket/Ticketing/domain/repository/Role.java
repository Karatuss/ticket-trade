package com.ticket.Ticketing.domain.repository;

import lombok.Getter;

@Getter
public enum Role {
    MANAGER("관리자"), MEMBER("사용자");

    private final String description;
    Role(String description){
        this.description = description;
    }
}
