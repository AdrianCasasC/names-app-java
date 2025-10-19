package com.example.names_app.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "names")
public class NameDto {
    @Id
    private String _id;
    private String name;
    private LocalDate dateOfMeet;
    private Boolean checked;
    private String details;
}
