package com.loginov.shorturl.model;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Table(name = "request")
@Entity
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "available_connections", nullable = false)
    private Integer availableConnections;

    @Column(name = "locked_until", nullable = false)
    private ZonedDateTime lockedUntil;

    @Column(name = "last_request",  nullable = false)
    private ZonedDateTime lastRequest;

    public ZonedDateTime getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(ZonedDateTime lastRequest) {
        this.lastRequest = lastRequest;
    }

    public ZonedDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(ZonedDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public Integer getAvailableConnections() {
        return availableConnections;
    }

    public void setAvailableConnections(Integer availableConnections) {
        this.availableConnections = availableConnections;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}