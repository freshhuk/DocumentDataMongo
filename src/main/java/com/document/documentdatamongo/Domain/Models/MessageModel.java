package com.document.documentdatamongo.Domain.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This model for request or response with document model
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageModel {

    @JsonProperty("status")
    private String status;
    @JsonProperty("documentModel")
    private DocumentDTO documentModel;
}
