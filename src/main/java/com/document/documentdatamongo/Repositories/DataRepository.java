package com.document.documentdatamongo.Repositories;

import com.document.documentdatamongo.Domain.Entities.DocumentModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataRepository extends MongoRepository<DocumentModel, ObjectId> {
    boolean existsByName(String name);
    DocumentModel findByName(String name);
    void deleteAll();
}
