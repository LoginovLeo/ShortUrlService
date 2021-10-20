package com.example.shorturl.repository;

import com.example.shorturl.model.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlRepo extends JpaRepository<UrlEntity, Integer> {

    @Query("SELECT u FROM UrlEntity u WHERE u.shortUrl = ?1")
    UrlEntity findUrlByShortUrl(String fullUrl);
}
