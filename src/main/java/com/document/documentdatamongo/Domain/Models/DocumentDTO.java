package com.document.documentdatamongo.Domain.Models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentDTO {
    private String fileName;
    private String fileType;
}
