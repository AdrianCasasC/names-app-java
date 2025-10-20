package com.example.names_app.repositories;

import com.example.names_app.models.NameDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NamesRepositoryInterface extends MongoRepository<NameDto, String> {
    NameDto findByName(String name);
}
