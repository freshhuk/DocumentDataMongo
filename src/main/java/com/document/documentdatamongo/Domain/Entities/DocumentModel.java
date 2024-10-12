package com.document.documentdatamongo.Domain.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "docs")
public class DocumentModel {

    @MongoId
    private int id;

    private String name;

    private String docType;

    private LocalDate createdDate;

    private LocalDate modifyDate;

    private int idUserCreate;

    private int idUserModify;
}
