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

    @RabbitListener(queues = "SecondQueue")
    public void receiveDocument(DocumentDTO documentDTO) {
        try{
            if (documentDTO != null){
                service.add(documentDTO);
                System.out.println("Success " + documentDTO.getFileName());
                sendMessage("FirstOkQueue", "Done");
            } else{
                sendMessage("FirstBadQueue", "Model is null");
                System.out.println("Error ");
            }
        } catch (Exception ex){
            System.out.println("Error from receiveDocument: " + ex);
            sendMessage("FirstBadQueue", "Error");
        }
    }

    private void sendMessage(String nameQueue, String status){
        rabbitTemplate.convertAndSend(nameQueue, status);
    }

}
