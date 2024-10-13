package com.document.documentdatamongo.Repositories;

import com.document.documentdatamongo.Domain.Entities.DocumentModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataRepository extends MongoRepository<DocumentModel, Integer> {
    boolean existsByName(String name);
    DocumentModel findByName(String name);
}
