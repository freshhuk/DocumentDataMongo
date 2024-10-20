package com.document.documentdatamongo.Domain.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "docs")
public class DocumentModel {

    @Id
    private ObjectId id;
    @Field
    private String name;
    @Field
    private String docType;
    @Field
    private LocalDate createdDate;
    @Field
    private LocalDate modifyDate;
    @Field
    private int idUserCreate;
    @Field
    private int idUserModify;
}
