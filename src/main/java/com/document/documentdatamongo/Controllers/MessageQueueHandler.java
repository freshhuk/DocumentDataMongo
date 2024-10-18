package com.document.documentdatamongo.Controllers;

import com.document.documentdatamongo.Domain.Models.DocumentDTO;
import com.document.documentdatamongo.Services.DataService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageQueueHandler {

    private final RabbitTemplate rabbitTemplate;
    private final DataService service;

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
                System.out.println("Success " + documentDTO.getFileName());
                sendMessage("StatusDataQueue", "Done");
            } else{
                sendMessage("StatusDataQueue", "Model is null");
                System.out.println("Error model is null");
            }
        } catch (Exception ex){
            System.out.println("Error from receiveDocument: " + ex);
            sendMessage("StatusDataQueue", "Error");
        }
    }
    @RabbitListener(queues = "StatusMongoQueue")
    public void receiveStatus(String status) {
        try{
            if(status.equals("Done")) {
                System.out.println("Status success");
            } else {
                System.out.println("Status bad");
                sendMessage("StatusDataQueue", "Bad");
            }
        } catch (Exception ex){
            sendMessage("StatusDataQueue", "ErrorReceiveStatus");
        }
    }
        private void sendMessage(String nameQueue, String status){
        rabbitTemplate.convertAndSend(nameQueue, status);
    }

}
