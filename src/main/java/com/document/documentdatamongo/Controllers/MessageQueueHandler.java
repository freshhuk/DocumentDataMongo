package com.document.documentdatamongo.Controllers;

import com.document.documentdatamongo.Domain.Enums.MessageAction;
import com.document.documentdatamongo.Domain.Enums.QueueStatus;
import com.document.documentdatamongo.Domain.Models.DocumentDTO;
import com.document.documentdatamongo.Domain.Models.MessageWrapper;
import com.document.documentdatamongo.Services.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageQueueHandler {

    @Value("${queueSecond.name}")
    private String mongoQueueName;

    private final RabbitTemplate rabbitTemplate;
    private final DataService service;
    private final static Logger logger = LoggerFactory.getLogger(MessageQueueHandler.class);

    @Autowired
    public MessageQueueHandler(RabbitTemplate rabbitTemplate, DataService service) {
        this.rabbitTemplate = rabbitTemplate;
        this.service = service;
    }

    @RabbitListener(queues = "MongoQueue")
    public void receiveDocument(MessageWrapper documentDTO) {
        try{
            DocumentDTO entity = (DocumentDTO) documentDTO.getPayload();

            MessageWrapper message;
            if (entity != null){
                message = new MessageWrapper(MessageAction.UPLOAD.toString(), QueueStatus.DONE.toString());

                service.add(entity);
                logger.info("Success " + entity.getFileName());

            } else{

                message = new MessageWrapper(MessageAction.UPLOAD.toString(), QueueStatus.BAD.toString());

                logger.warn("Error model is null");

            }
            sendMessage(mongoQueueName, message);
        } catch (Exception ex){

            MessageWrapper message = new MessageWrapper(MessageAction.UPLOAD.toString(), QueueStatus.BAD.toString());

            logger.error("Error from receiveDocument: " + ex);
            sendMessage(mongoQueueName, message);
        }
    }
    @RabbitListener(queues = "StatusDataQueue")
    public void receiveStatus(MessageWrapper message) {
        try{
            String receivedStatus = (String) message.getPayload();

            if(receivedStatus.equals(QueueStatus.DONE.toString())) {
                logger.info("Receive status success");
            }
        } catch (Exception ex){

            MessageWrapper sentMessage = new MessageWrapper(MessageAction.UPLOAD.toString(), QueueStatus.BAD.toString());

            sendMessage(mongoQueueName, sentMessage);
            logger.error("Error Receive Status");
        }
    }
        private void sendMessage(String nameQueue, MessageWrapper message){
        rabbitTemplate.convertAndSend(nameQueue, message);
    }

}
