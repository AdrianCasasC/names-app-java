package com.example.names_app.repositories;

import com.example.names_app.models.PunctuationDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PunctuationRepositoryInterface extends MongoRepository<PunctuationDto, String> {
}
