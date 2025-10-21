package com.example.names_app.models;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "names")
public class NameDto {
    private String _id;
    private String name;
    private LocalDate date;
    private Boolean checkedByAdri;
    private Boolean checkedByElena;
    private String meaning;
    private String details;
}
