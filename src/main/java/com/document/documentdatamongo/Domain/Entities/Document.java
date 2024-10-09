package com.document.documentdatamongo.Domain.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    private int id;

    private String name;

    private String docType;

    private LocalDate createdDate;

    private LocalDate modifyDate;

    private int idUserCreate;

    private int idUserModify;
}
