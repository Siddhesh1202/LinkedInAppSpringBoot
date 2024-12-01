package com.siddhesh.linkedin.notification_service.clients;

import com.siddhesh.linkedin.notification_service.dto.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "connection-service", path = "/connection")
public interface ConnectionClient {
    @GetMapping("/core/firstConnections")
    List<PersonDto> getFirstConnections(@RequestHeader("X-User-Id") Long userId);
}
