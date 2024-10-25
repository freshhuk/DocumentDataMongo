package com.document.documentdatamongo.Controllers;

import com.document.documentdatamongo.Domain.Enums.QueueStatus;
import com.document.documentdatamongo.Domain.Models.DocumentDTO;
import com.document.documentdatamongo.Services.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageQueueHandler {

    private final RabbitTemplate rabbitTemplate;
    private final DataService service;
    private final static Logger logger = LoggerFactory.getLogger(MessageQueueHandler.class);

    @Autowired
    public MessageQueueHandler(RabbitTemplate rabbitTemplate, DataService service) {
        this.rabbitTemplate = rabbitTemplate;
        this.service = service;
    }

    @RabbitListener(queues = "MongoQueue")
    public void receiveDocument(DocumentDTO documentDTO) {
        try{
            if (documentDTO != null){
                service.add(documentDTO);
                logger.info("Success " + documentDTO.getFileName());
                sendMessage("StatusMongoQueue", QueueStatus.DONE.toString());
            } else{
                logger.warn("Error model is null");
                sendMessage("StatusMongoQueue", QueueStatus.BAD + documentDTO.getFileName());
            }
        } catch (Exception ex){
            logger.error("Error from receiveDocument: " + ex);
            sendMessage("StatusMongoQueue", QueueStatus.BAD + documentDTO.getFileName());
        }
    }
    @RabbitListener(queues = "StatusDataQueue")
    public void receiveStatus(String status) {
        try{
            if(status.equals(QueueStatus.DONE.toString())) {
                logger.info("Receive status success");
            }
        } catch (Exception ex){
            sendMessage("StatusMongoQueue", QueueStatus.BAD.toString());
            logger.error("Error Receive Status");
        }
    }
        private void sendMessage(String nameQueue, String status){
        rabbitTemplate.convertAndSend(nameQueue, status);
    }

}
