package com.siddhesh.linkedin.connection_service.service;

import com.siddhesh.linkedin.connection_service.auth.UserContextHolder;
import com.siddhesh.linkedin.connection_service.entity.Person;
import com.siddhesh.linkedin.connection_service.event.AcceptConnectionRequestEvent;
import com.siddhesh.linkedin.connection_service.event.SendConnectionRequestEvent;
import com.siddhesh.linkedin.connection_service.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionService {
    private final PersonRepository personRepository;
    private final KafkaTemplate<Long, SendConnectionRequestEvent> sendConnectionRequestKafkaTemplate;
    private final KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptConnectionRequestEventKafkaTemplate;

    public List<Person> getFirstDegreeConnections() {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("getting First Degree Connections for {}", userId);
        return personRepository.getFirstDegreeConnections(userId);
    }

    public Boolean sendConnectionRequest(Long userId) {
        Long senderId = UserContextHolder.getCurrentUserId();
        log.info("sending connection request from {} to {}", senderId, userId);
        if(senderId.equals(userId)){
            throw new RuntimeException("Sender and Receiver are equal");
        }

        boolean alreadySentRequest = personRepository.connectionExists(senderId, userId);
        if (alreadySentRequest) {
            throw new RuntimeException("Connection request already sent");
        }

        boolean alreadyConnected = personRepository.alreadyConnected(senderId, userId);
        if (alreadyConnected) {
            throw new RuntimeException("Connection already exists");
        }
        log.info("connection request sent");
        personRepository.addConnectionRequest(senderId, userId);

        SendConnectionRequestEvent sendConnectionRequestEvent = SendConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(userId)
                .build();
        sendConnectionRequestKafkaTemplate.send("send-connection-requests-topic", sendConnectionRequestEvent);
        return true;
    }


    public Boolean acceptConnectionRequest(Long userId) {
        Long receiverId = UserContextHolder.getCurrentUserId();
        log.info("accepting connection request from {} to {}", receiverId, userId);
        if(receiverId.equals(userId)){
            throw new RuntimeException("Receiver and Sender are equal");
        }

        boolean connectionRequestExists = personRepository.connectionExists(userId, receiverId);
        if (!connectionRequestExists) {
            throw new RuntimeException("Connection request does not exist");
        }

        boolean alreadyConnected = personRepository.alreadyConnected(userId, receiverId);
        if (alreadyConnected) {
            throw new RuntimeException("Connection already exists");
        }

        log.info("connection request accepted");
        personRepository.acceptConnectionRequest(userId, receiverId);

        AcceptConnectionRequestEvent acceptConnectionRequestEvent = AcceptConnectionRequestEvent.builder()
                .senderId(userId)
                .receiverId(receiverId)
                .build();
        acceptConnectionRequestEventKafkaTemplate.send("accept-connection-requests-topic", acceptConnectionRequestEvent);

        return true;

    }

    public Boolean rejectConnectionRequest(Long userId) {
        Long receiverId = UserContextHolder.getCurrentUserId();
        log.info("rejecting connection request from {} to {}", receiverId, userId);
        if(receiverId.equals(userId)){
            throw new RuntimeException("Receiver and Sender are equal");
        }

        boolean connectionRequestExists = personRepository.connectionExists(userId, receiverId);
        if (!connectionRequestExists) {
            throw new RuntimeException("Connection request does not exist");
        }

        personRepository.rejectConnectionRequest(userId, receiverId);
        return true;
    }
}
