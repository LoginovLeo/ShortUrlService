package com.loginov.shorturl.repository;

import com.loginov.shorturl.model.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface UrlRepo extends JpaRepository<UrlEntity, Integer> {

    @Query("SELECT u FROM UrlEntity u WHERE u.shortUrl = ?1 AND u.expiresTime > ?2")
    UrlEntity findUrlByShortUrl(String fullUrl, ZonedDateTime zonedDateTime);

    @Query("SELECT u FROM UrlEntity u WHERE u.shortUrl = ?1")
    List<UrlEntity> findAllByShortUrl(String fullUrl);

    @Query("SELECT u FROM UrlEntity u WHERE u.originalUrl = ?1 AND u.expiresTime > ?2")
    UrlEntity findUrlEntityByOriginalUrlAndExpiresTime(String fullUrl, ZonedDateTime zonedDateTime);
}
