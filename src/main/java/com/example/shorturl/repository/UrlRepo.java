package com.example.shorturl.repository;

import com.example.shorturl.model.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepo extends JpaRepository<UrlEntity, Integer> {

}
