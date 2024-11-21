package com.document.documentdatamongo.Services;

import com.document.documentdatamongo.Domain.Entities.DocumentModel;
import com.document.documentdatamongo.Domain.Models.DocumentDTO;
import com.document.documentdatamongo.Repositories.DataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DataService {

    private final DataRepository repository;
    private final static Logger logger = LoggerFactory.getLogger(DataService.class);

    @Autowired
    public DataService(DataRepository repository){
        this.repository = repository;
    }

    public void add(DocumentDTO documentDTO) {
        try{
            if (!repository.existsByName(documentDTO.getFileName())) {

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
                logger.info("Entity was added in db");
            } else {
                logger.info("Entity was updated in db");
                updateDocument(documentDTO);
            }
        } catch (Exception ex){
            logger.error("Error from add method" + ex);
        }
    }

    public void deleteAllDocuments(){repository.deleteAll();}
    private void updateDocument(DocumentDTO doc) {
        try {
            DocumentModel document = repository.findByName(doc.getFileName());
            LocalDate currentDate = LocalDate.now();
            document.setModifyDate(currentDate);
            document.setIdUserModify(1);//todo

            repository.save(document);
        } catch (Exception ex) {
            logger.error("Error in updateDocument method " + ex);
        }
    }

}
