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
    public void receiveDocument(MessageWrapper<DocumentDTO> documentDTO) {
        try{
            DocumentDTO entity = documentDTO.getPayload();
            System.out.println(entity);
            MessageWrapper<DocumentDTO> sentModel;
            MessageWrapper<MessageWrapper<DocumentDTO>> message;// Заместь вложеного просто создать новую модель с новыми полями которые
           // включают все не обходимое потом настроить его в приемнеке новой дизенчтото там и все


            if (entity != null){
                sentModel = new MessageWrapper<>(QueueStatus.DONE.toString(), entity);
                message = new MessageWrapper<>(MessageAction.UPLOAD.toString(), sentModel);

                service.add(entity);
                logger.info("Success " + entity.getFileName());

            } else{
                sentModel = new MessageWrapper<>(QueueStatus.BAD.toString(), null);
                message = new MessageWrapper<>(MessageAction.UPLOAD.toString(), sentModel);
                logger.warn("Error model is null");
            }
            sendMessage(mongoQueueName, message);
        } catch (Exception ex){

            DocumentDTO entity = documentDTO.getPayload();

            MessageWrapper<DocumentDTO> sentModel = new MessageWrapper<>(QueueStatus.BAD.toString(), entity);

            MessageWrapper<MessageWrapper<DocumentDTO>> message = new MessageWrapper<>(MessageAction.UPLOAD.toString(), sentModel);

            logger.error("Error from receiveDocument: " + ex);
            sendMessage(mongoQueueName, message);
        }
    }
    @RabbitListener(queues = "StatusDataQueue")
    public void receiveStatus(MessageWrapper<String> message) {
        try{
            String receivedStatus = message.getPayload();

            if(receivedStatus.equals(QueueStatus.DONE.toString())) {
                logger.info("Receive status success");
            }
        } catch (Exception ex){

            MessageWrapper<String> sentMessage = new MessageWrapper<>(MessageAction.UPLOAD.toString(), QueueStatus.BAD.toString());

            sendMessage(mongoQueueName, sentMessage);
            logger.error("Error Receive Status");
        }
    }
        private void sendMessage(String nameQueue, MessageWrapper<?> message){
        rabbitTemplate.convertAndSend(nameQueue, message);
    }

}
