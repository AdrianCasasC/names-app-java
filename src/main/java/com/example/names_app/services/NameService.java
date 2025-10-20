package com.example.names_app.services;

import com.example.names_app.models.NameDto;
import com.example.names_app.models.NameGroupDto;
import com.example.names_app.repositories.NamesRepositoryInterface;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import static java.util.List.of;

@Service
public class NameService {
    private final NamesRepositoryInterface repository;
    private final MongoClient mongoClient;

    @Value("${MONGO_DB_NAME}")
    private String mongoDbName;

    //@Value("${MONGO_COLL_NAME}")
    //private String mongoCollName;

    public NameService(NamesRepositoryInterface repository, MongoClient mongoClient) {
        this.repository = repository;
        this.mongoClient = mongoClient;
    }

    public List<NameDto> getAll() {
        List<NameDto> names = repository.findAll();
        return names;
    }

    public List<NameGroupDto> getNamesGroupedByFirstLetter() {
        MongoDatabase database = mongoClient.getDatabase(mongoDbName);
        MongoCollection<Document> collection = database.getCollection("names");

        List<NameGroupDto> groupedNames = new ArrayList<>();

        var pipeline = of(
                new Document("$group", new Document("_id",
                        new Document("$toUpper", new Document("$substrCP", of("$name", 0, 1))))
                        .append("names", new Document("$push", "$name"))),
                new Document("$sort", new Document("_id", 1))
        );

        for (Document doc : collection.aggregate(pipeline)) {
            String letter = doc.getString("_id");
            List<String> names = (List<String>) doc.get("names");
            groupedNames.add(new NameGroupDto(letter, names));
        }

        return groupedNames;
    }

    public NameDto getByName(String name) {
        NameDto foundName = repository.findByName(name);
        return foundName;
    }
}
