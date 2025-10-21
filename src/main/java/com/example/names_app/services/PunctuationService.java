package com.example.names_app.services;

import com.example.names_app.models.PunctuationDto;
import com.example.names_app.repositories.PunctuationRepositoryInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PunctuationService {
    private final PunctuationRepositoryInterface repository;

    public PunctuationService (PunctuationRepositoryInterface repository) {
        this.repository = repository;
    }

    public List<PunctuationDto> getAll() {
        return repository.findAll();
    }
}
