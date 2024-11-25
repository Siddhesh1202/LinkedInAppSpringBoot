package com.siddhesh.linkedin.posts_service.clients;

import com.siddhesh.linkedin.posts_service.dto.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "connection-service", path = "/connection")
public interface ConnectionClient {
    @GetMapping("/core/firstConnections")
    List<PersonDto> getFirstConnections();
}
