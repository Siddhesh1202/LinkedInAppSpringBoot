package com.siddhesh.linkedin.connection_service.controller;

import com.siddhesh.linkedin.connection_service.entity.Person;
import com.siddhesh.linkedin.connection_service.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class ConnectionController {
    private final ConnectionService connectionService;
    @GetMapping("/{userId}/firstConnections")
    public ResponseEntity<List<Person>> getFirstConnections(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(connectionService.getFirstDegreeConnections(userId));
    }


}
