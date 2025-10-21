package com.example.names_app.controllers;

import com.example.names_app.models.NameDto;
import com.example.names_app.models.NameGroupDto;
import com.example.names_app.models.PaginatedNamesDto;
import com.example.names_app.models.PunctuationDto;
import com.example.names_app.services.NameService;
import com.example.names_app.services.PunctuationService;
import com.mongodb.lang.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/punctuation")
public class PunctuationController {

    private final PunctuationService service;

    public PunctuationController(PunctuationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PunctuationDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
