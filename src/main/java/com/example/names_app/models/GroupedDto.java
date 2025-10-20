package com.example.names_app.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GroupedDto {
    public String letter;
    public List<NameDto> list;
}
