package com.example.names_app.models;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "punctuation")
public class PunctuationDto {
    private ObjectId _id;
    private String identity;
    private Integer total;
    @Field("names_checked")
    private List<String> namesChecked;
}
