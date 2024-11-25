package com.siddhesh.linkedin.connection_service.controller;

import com.siddhesh.linkedin.connection_service.entity.Person;
import com.siddhesh.linkedin.connection_service.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class ConnectionController {
    private final ConnectionService connectionService;
    @GetMapping("/firstConnections")
    public ResponseEntity<List<Person>> getFirstConnections(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(connectionService.getFirstDegreeConnections(userId));
    }


}
