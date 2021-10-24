package com.loginov.shorturl.service;

import com.loginov.shorturl.exeption.customexceptions.CustomBadRequestException;
import com.loginov.shorturl.model.RequestEntity;
import com.loginov.shorturl.repository.RequestRepo;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class RequestService {

    private final RequestRepo requestRepo;

    public RequestService(RequestRepo requestRepo) {
        this.requestRepo = requestRepo;
    }

    public void updateRequest(RequestEntity request){
        requestRepo.save(request);
    }

    public RequestEntity getSessions() {
    return requestRepo.getById(1);
    }

    public void chekSessions(){

        RequestEntity sessions = requestRepo.getById(1);

        if (sessions.getLastRequest().isBefore(ZonedDateTime.now().minusSeconds(30))){
            sessions.setAvailableConnections(30);
            sessions.setLastRequest(ZonedDateTime.now());
            requestRepo.save(sessions);
        }

        if(sessions.getLockedUntil().isAfter(ZonedDateTime.now())){
            throw new CustomBadRequestException("All blocked");
        }

        if(sessions.getAvailableConnections() == 0){
            sessions.setLockedUntil(ZonedDateTime.now().plusSeconds(30));
            sessions.setAvailableConnections(30);
            requestRepo.save(sessions);
            throw new CustomBadRequestException("All blocked");
        }
        if (sessions.getAvailableConnections() > 0 ){
            Integer availableConnections = sessions.getAvailableConnections();
            availableConnections--;
            sessions.setLastRequest(ZonedDateTime.now());
            sessions.setAvailableConnections(availableConnections);
        }
        requestRepo.save(sessions);
    }
}
