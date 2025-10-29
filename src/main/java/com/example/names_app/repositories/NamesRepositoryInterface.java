package com.example.names_app.repositories;

import com.example.names_app.models.NameDto;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NamesRepositoryInterface extends MongoRepository<NameDto, ObjectId> {
    NameDto findByName(String name);
}
