package com.example.names_app.services;

import com.example.names_app.models.GroupedDto;
import com.example.names_app.models.NameDto;
import com.example.names_app.models.NameGroupDto;
import com.example.names_app.models.PaginatedNamesDto;
import com.example.names_app.repositories.NamesRepositoryInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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

    public PaginatedNamesDto getAll(String coincidence, int page, int pageSize) {
        MongoDatabase database = mongoClient.getDatabase(mongoDbName);
        MongoCollection<Document> collection = database.getCollection("names");

        if (page < 1) page = 1;
        int skip = (page - 1) * pageSize;

        // Build filter
        Document filter = new Document();
        if (coincidence != null && !coincidence.isEmpty()) {
            filter = new Document("name", new Document("$regex", coincidence).append("$options", "i"));
        }

        long totalElements = collection.countDocuments(filter);

        // Fetch paginated documents from MongoDB
        FindIterable<Document> iterable = collection.find(filter)
                .sort(Sorts.ascending("name"))
                .skip(skip)
                .limit(pageSize);

        List<Document> docs = new ArrayList<>();
        iterable.into(docs);

        // Convert MongoDB Document to Person POJO using Jackson
        ObjectMapper mapper = new ObjectMapper();
        List<NameDto> people = docs.stream().map(d -> {
            NameDto p = new NameDto();
            // Convert ObjectId to String if your Person._id is String
            ObjectId id = d.getObjectId("_id");
            p.set_id(id != null ? id.toHexString() : null);

            p.setName(d.getString("name"));

            // If you have a date field stored as ISODate in MongoDB
            Object dateObj = d.get("date");
            if (dateObj instanceof Date) {
                p.setDate(((Date) dateObj).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }

            p.setCheckedByAdri(d.getBoolean("checkedByAdri", false));
            p.setCheckedByElena(d.getBoolean("checkedByElena", false));
            p.setMeaning(d.getString("meaning"));
            p.setDetails(d.getString("details"));

            return p;
        }).collect(Collectors.toList());

        // Group by first letter
        Map<String, List<NameDto>> groupedMap = people.stream()
                .collect(Collectors.groupingBy(p -> p.getName().substring(0, 1).toUpperCase()));

        // Convert to list of Grouped objects
        List<GroupedDto> groupedList = groupedMap.entrySet().stream()
                .map(e -> new GroupedDto(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(g -> g.letter))
                .collect(Collectors.toList());

        return new PaginatedNamesDto(groupedList, page, pageSize, totalElements);
    }

    public List<NameGroupDto> getNamesGroupedByFirstLetter(String coincidence, int page, int pageSize) {
        MongoDatabase database = mongoClient.getDatabase(mongoDbName);
        MongoCollection<Document> collection = database.getCollection("names");

        List<NameGroupDto> groupedNames = new ArrayList<>();

        List<Document> pipeline = new ArrayList<>();

        // Stage 1: Filter by 'coincidence' string if it's not null or empty
        if (coincidence != null && !coincidence.isEmpty()) {
            pipeline.add(new Document("$match",
                    new Document("name", new Document("$regex", coincidence).append("$options", "i"))));
        }

        // Stage 2: Group by first letter
        pipeline.add(new Document("$group", new Document("_id",
                new Document("$toUpper", new Document("$substrCP", of("$name", 0, 1))))
                .append("names", new Document("$push", "$name"))));

        // Stage 3: Sort by letter
        pipeline.add(new Document("$sort", new Document("_id", 1)));

        // Stage 4: Pagination (skip & limit)
        // Mongo page numbers usually start from 1
        if (page < 1) page = 1;
        if (pageSize <= 0) pageSize = 10;
        int skip = (page - 1) * pageSize;
        pipeline.add(new Document("$skip", skip));
        pipeline.add(new Document("$limit", pageSize));

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
