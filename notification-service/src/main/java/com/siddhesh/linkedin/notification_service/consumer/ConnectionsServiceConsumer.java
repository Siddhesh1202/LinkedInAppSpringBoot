package com.siddhesh.linkedin.notification_service.consumer;

import com.siddhesh.linkedin.connection_service.event.AcceptConnectionRequestEvent;
import com.siddhesh.linkedin.connection_service.event.SendConnectionRequestEvent;
import com.siddhesh.linkedin.notification_service.service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionsServiceConsumer {
    private final SendNotification sendNotification;

    @KafkaListener(topics = "send-connection-requests-topic")
    public void handleSendConnectionRequest(SendConnectionRequestEvent sendConnectionRequestEvent) {
        log.info("Received send connection request");
        String message = "You have received a send connection request from user with id: %d" + sendConnectionRequestEvent.getSenderId();
        sendNotification.send(sendConnectionRequestEvent.getReceiverId(), message);
    }

    @KafkaListener(topics = "accept-connection-requests-topic")
    public void handleAcceptConnectionRequest(AcceptConnectionRequestEvent acceptConnectionRequestEvent) {
        log.info("Received accept connection request");
        String message = "Your connection request has been accepted by user with id: %d" + acceptConnectionRequestEvent.getReceiverId();
        sendNotification.send(acceptConnectionRequestEvent.getSenderId(), message);

    }

}
