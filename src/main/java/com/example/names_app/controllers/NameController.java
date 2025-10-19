package com.example.names_app.controllers;

import com.example.names_app.models.NameDto;
import com.example.names_app.services.NameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/names")
public class NameController {

    private final NameService service;

    public NameController(NameService service) {
        this.service = service;
    }

    @GetMapping
    public List<NameDto> getAll() {
        List<NameDto> names = service.getAll();
        System.out.println("Names -> " +  names.getFirst().toString());
        return names;
    }
}
