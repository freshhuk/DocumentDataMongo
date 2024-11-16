package com.document.documentdatamongo.Domain.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageWrapper<T> {
    @JsonProperty("action")
    private String action;
    @JsonProperty("payload")
    private T payload;
}