package com.loginov.shorturl.service;

import com.loginov.shorturl.exeption.customexceptions.CustomToManyRequestException;
import com.loginov.shorturl.model.RequestEntity;
import com.loginov.shorturl.repository.RequestRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
public class RequestService {
    @Value("${request.maxlimit}")
    private int limitRequests;
    @Value("${request.timelimit}")
    private int timeLimit;
    @Value("${request.blocktime}")
    private int blockTime;

    private final int ID = 1;

    private final RequestRepo requestRepo;

    public RequestService(RequestRepo requestRepo) {
        this.requestRepo = requestRepo;
    }

    public void updateRequest(RequestEntity request) {
        requestRepo.save(request);
    }

    public RequestEntity getSessions() {
        return requestRepo.getById(ID);
    }

    public void chekSessions() {

        RequestEntity sessions = requestRepo.getById(ID);


        if (sessions.getLastRequest().isBefore(ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(timeLimit))) {
            sessions.setAvailableConnections(limitRequests);
            sessions.setLastRequest(ZonedDateTime.now(ZoneOffset.UTC));
            requestRepo.save(sessions);
        }

        if (sessions.getLockedUntil().isAfter(ZonedDateTime.now(ZoneOffset.UTC))) {
            throw new CustomToManyRequestException("Too many requests. Please try later");
        }

        if (sessions.getAvailableConnections() == 0) {
            sessions.setLockedUntil(ZonedDateTime.now(ZoneOffset.UTC).plusSeconds(blockTime));
            sessions.setAvailableConnections(limitRequests);
            requestRepo.save(sessions);
            throw new CustomToManyRequestException("Too many requests. Please try later");
        }

        if (sessions.getAvailableConnections() > 0) {
            Integer availableConnections = sessions.getAvailableConnections();
            availableConnections--;
            sessions.setLastRequest(ZonedDateTime.now(ZoneOffset.UTC));
            sessions.setAvailableConnections(availableConnections);
        }
        requestRepo.save(sessions);
    }
}
