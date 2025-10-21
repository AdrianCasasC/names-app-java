package com.example.names_app.services;

import com.example.names_app.constants.Identity;
import com.example.names_app.models.PunctuationDto;
import com.example.names_app.repositories.PunctuationRepositoryInterface;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PunctuationService {
    private final PunctuationRepositoryInterface repository;
    private final MongoClient mongoClient;

   /* @Value("${MONGO_URI}")
    private String mongoUri;*/
    @Value("${MONGO_DB_NAME}")
    private String mongoDbName;
    @Value("${MONGO_PUNCT_COLL}")
    private String mongoPunctCollection;

    public PunctuationService (PunctuationRepositoryInterface repository, MongoClient mongoClient) {
        this.repository = repository;
        this.mongoClient = mongoClient;
    }

    public List<PunctuationDto> getAll() {
        return repository.findAll();
    }

    public void setPunctuation(Identity identity, String name, Boolean checked) {
        MongoDatabase database = mongoClient.getDatabase(mongoDbName);
        MongoCollection<Document> collection = database.getCollection(mongoPunctCollection);

        Bson filter = Filters.eq("identity", identity);

        Bson update;

        if (Boolean.TRUE.equals(checked)) {
            // ✅ If checked = true → add 1 to total and add name to array
            update = Updates.combine(
                    Updates.inc("total", 1),
                    Updates.addToSet("names_checked", name)
            );
        } else {
            // 🚫 If checked = false → subtract 1 from total and remove name from array
            update = Updates.combine(
                    Updates.inc("total", -1),
                    Updates.pull("names_checked", name)
            );
        }

        collection.updateOne(filter, update);
    }
}
