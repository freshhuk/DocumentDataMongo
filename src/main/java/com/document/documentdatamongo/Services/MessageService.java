package com.document.documentdatamongo.Services;

import com.document.documentdatamongo.Domain.Enums.MessageAction;
import com.document.documentdatamongo.Domain.Enums.QueueStatus;
import com.document.documentdatamongo.Domain.Models.DocumentDTO;
import com.document.documentdatamongo.Domain.Models.MessageModel;
import com.document.documentdatamongo.Domain.Models.MessageWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageService {


    @Value("${queue.name}")
    private String mongoStatusQueueName;

    private final RabbitTemplate rabbitTemplate;
    private final DataService service;
    private final static Logger logger = LoggerFactory.getLogger(MessageService.class);
    @Autowired
    public MessageService(RabbitTemplate rabbitTemplate, DataService service) {
        this.rabbitTemplate = rabbitTemplate;
        this.service = service;
    }

    /**
     * Method get entity, and add this entity on mongo db
     * @param documentDTO message
     */
    public void sendFinalUploadStatus(MessageWrapper<DocumentDTO> documentDTO){
        try{
            DocumentDTO entity = documentDTO.getPayload();
            System.out.println(documentDTO);
            MessageWrapper<MessageModel> message;


            if (entity != null){
                MessageModel sentModel = new MessageModel(QueueStatus.DONE.toString(), entity);
                message = new MessageWrapper<>(MessageAction.UPLOAD.toString(), sentModel);

                service.add(entity);
                logger.info("Success " + entity.getFileName());

            } else{
                MessageModel sentModel = new MessageModel(QueueStatus.BAD.toString(), null);
                message = new MessageWrapper<>(MessageAction.UPLOAD.toString(), sentModel);
                logger.warn("Error model is null");
            }
            sendMessage(mongoStatusQueueName, message);

        } catch (Exception ex){

            DocumentDTO entity = documentDTO.getPayload();

            MessageModel sentModel = new MessageModel(QueueStatus.BAD.toString(), entity);

            MessageWrapper<MessageModel> message = new MessageWrapper<>(MessageAction.UPLOAD.toString(), sentModel);

            logger.error("Error from receiveDocument: " + ex);
            sendMessage(mongoStatusQueueName, message);
        }
    }

    /**
     * Method for deleting all entity on db
     */
    public void sendFinalDeleteStatus(){
        try{
            service.deleteAllDocuments();
            MessageWrapper<String> message = new MessageWrapper<>(MessageAction.DELETE.toString(), QueueStatus.DONE.toString());
            sendMessage(mongoStatusQueueName, message);
            logger.info("Entities were deleted successful");
        } catch (Exception ex){
            logger.error("Error with deleting " + ex);
            MessageWrapper<String> message = new MessageWrapper<>(MessageAction.DELETE.toString(), QueueStatus.BAD.toString());
            sendMessage(mongoStatusQueueName, message);

        }
    }

    /**
     * Method get status from postgres microservice
     * @param message message
     */
    public void processingStatus(MessageWrapper<String> message){
        try{
            String receivedStatus = message.getPayload();

            if(receivedStatus.equals(QueueStatus.DONE.toString())) {
                logger.info("Receive status success");
            }
        } catch (Exception ex){

            MessageWrapper<String> sentMessage = new MessageWrapper<>(MessageAction.UPLOAD.toString(), QueueStatus.BAD.toString());

            sendMessage(mongoStatusQueueName, sentMessage);
            logger.error("Error Receive Status");
        }
    }

    private void sendMessage(String nameQueue, MessageWrapper<?> message){
        rabbitTemplate.convertAndSend(nameQueue, message);
    }
}
