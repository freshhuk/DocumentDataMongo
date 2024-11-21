package com.document.documentdatamongo.Controllers;

import com.document.documentdatamongo.Domain.Enums.MessageAction;
import com.document.documentdatamongo.Domain.Models.DocumentDTO;
import com.document.documentdatamongo.Domain.Models.MessageWrapper;
import com.document.documentdatamongo.Services.MessageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageQueueHandler {

    private final MessageService messageService;

    @Autowired
    public MessageQueueHandler( MessageService messageService) {
        this.messageService = messageService;
    }

    @RabbitListener(queues = "MongoQueue")
    public void receiveDocument(MessageWrapper<DocumentDTO> documentDTO) {
        if(documentDTO.getAction().equals(MessageAction.UPLOAD.toString())){
            messageService.sendFinalUploadStatus(documentDTO);
        }
    }
    @RabbitListener(queues = "StatusDataQueue")
    public void receiveStatus(MessageWrapper<String> message) {
        if(message.getAction().equals(MessageAction.UPLOAD.toString())){
           messageService.processingStatus(message);
        } else if (message.getAction().equals(MessageAction.DELETE.toString())) {
            messageService.sendFinalDeleteStatus();
        }

    }
}
