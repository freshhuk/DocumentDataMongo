package com.document.documentdatamongo.Services;

import com.document.documentdatamongo.Domain.Entities.DocumentModel;
import com.document.documentdatamongo.Domain.Models.DocumentDTO;
import com.document.documentdatamongo.Repositories.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DataService {

    private final DataRepository repository;

    @Autowired
    public DataService(DataRepository repository){
        this.repository = repository;
    }

    public void add(DocumentDTO documentDTO) {
        try{
            if (shouldCreateNewDocument(documentDTO)) {

                LocalDate currentDate = LocalDate.now();

                DocumentModel document = new DocumentModel();
                {
                    document.setName(documentDTO.getFileName());
                    document.setDocType(documentDTO.getFileType());
                    document.setCreatedDate(currentDate);
                    document.setModifyDate(currentDate);
                }
                document.setIdUserCreate(1);//todo
                document.setIdUserModify(1);//todo

                repository.save(document);
            } else {
                updateDocument(documentDTO);
            }
        } catch (Exception ex){
            System.out.println("Error from dd method");
        }
    }
    private void updateDocument(DocumentDTO doc) {
        try {
            DocumentModel document = repository.findByName(doc.getFileName());
            LocalDate currentDate = LocalDate.now();
            document.setModifyDate(currentDate);
            document.setIdUserModify(1);//todo

            repository.save(document);
        } catch (Exception ex) {
            System.out.println("Error in updateDocument method " + ex);
        }
    }

    private boolean shouldCreateNewDocument(DocumentDTO doc) {
        if (!repository.existsByName(doc.getFileName())) {
            String lastFourCharacters = doc.getFileName().substring(doc.getFileName().length() - 5);
            return lastFourCharacters.equals(".docx");
        }
        return false;
    }
}
