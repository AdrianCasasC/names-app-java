package com.example.names_app.services;

import com.example.names_app.models.NameDto;
import com.example.names_app.repositories.NamesRepositoryInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NameService {
    private final NamesRepositoryInterface repository;

    public NameService(NamesRepositoryInterface repository) {
        this.repository = repository;
    }

    public List<NameDto> getAll() {
        List<NameDto> names = repository.findAll();
        return names;
    }
}
