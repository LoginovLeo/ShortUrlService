package com.loginov.shorturl.repository;

import com.loginov.shorturl.model.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepo extends JpaRepository<RequestEntity,Integer> {
}
