package com.example.names_app.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class NameGroupDto {
    private String letter;
    private List<String> names;
}
