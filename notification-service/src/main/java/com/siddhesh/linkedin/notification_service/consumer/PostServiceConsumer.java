package com.siddhesh.linkedin.notification_service.consumer;

import com.siddhesh.linkedin.notification_service.clients.ConnectionClient;
import com.siddhesh.linkedin.notification_service.dto.PersonDto;
import com.siddhesh.linkedin.notification_service.entity.Notification;
import com.siddhesh.linkedin.notification_service.repository.NotificationRepository;
import com.siddhesh.linkedin.notification_service.service.SendNotification;
import com.siddhesh.linkedin.posts_service.event.PostCreatedEvent;
import com.siddhesh.linkedin.posts_service.event.PostLikeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceConsumer {
    private final ConnectionClient connectionClient;
    private final NotificationRepository notificationRepository;
    private final SendNotification sendNotification;

    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent){
        log.info("Sending Notifications: handlePostCreated");
        List<PersonDto> connections = connectionClient.getFirstConnections(postCreatedEvent.getCreatorId());

        for (PersonDto connection : connections) {
            sendNotification.send(connection.getUserId(), "Your Connections " + postCreatedEvent.getCreatorId() + " has created Post");
        }
    }
    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLiked(PostLikeEvent postLikeEvent){
        log.info("Sending Notifications: handlePostLiked: {}", postLikeEvent);
        String message = String.format("Your post, %d has been liked by %d", postLikeEvent.getPostId(), postLikeEvent.getLikedByUserId());
        sendNotification.send(postLikeEvent.getCreatorId(), message);
    }

}
