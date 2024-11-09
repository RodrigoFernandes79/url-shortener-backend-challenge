package com.tds.url_shortener.repository;

import com.tds.url_shortener.domain.entity.UrlEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlEntityRepository extends MongoRepository<UrlEntity,String> {
}
