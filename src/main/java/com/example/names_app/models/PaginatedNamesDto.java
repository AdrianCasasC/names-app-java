package com.example.names_app.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginatedNamesDto {
    public List<GroupedDto> groupedNames;
    public Integer page;
    public Integer pageSize;
    public Long total;
}
