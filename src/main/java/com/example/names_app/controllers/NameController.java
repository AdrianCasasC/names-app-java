package com.example.names_app.controllers;

import com.example.names_app.models.GroupedDto;
import com.example.names_app.models.NameDto;
import com.example.names_app.models.NameGroupDto;
import com.example.names_app.models.PaginatedNamesDto;
import com.example.names_app.services.NameService;
import com.mongodb.lang.Nullable;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/names")
public class NameController {

    private final NameService service;

    public NameController(NameService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<PaginatedNamesDto> getAll(@Nullable @RequestParam String coincidence, @RequestParam int page, @RequestParam int pageSize) {
        return ResponseEntity.ok(service.getAll(coincidence, page, pageSize));
    }

    @GetMapping("/grouped")
    public ResponseEntity<List<NameGroupDto>> getGroupedNames(@Nullable @RequestParam String coincidence, @RequestParam int page, @RequestParam int pageSize) {
        List<NameGroupDto> result = service.getNamesGroupedByFirstLetter(coincidence, page, pageSize);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{name}")
    public ResponseEntity<NameDto> getByName(@PathVariable String name) {
        return ResponseEntity.ok(service.getByName(name));
    }
}
