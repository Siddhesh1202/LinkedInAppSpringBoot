package com.siddhesh.linkedin.connection_service.service;

import com.siddhesh.linkedin.connection_service.entity.Person;
import com.siddhesh.linkedin.connection_service.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionService {
    private final PersonRepository personRepository;

    public List<Person> getFirstDegreeConnections(Long userId) {
        log.info("getting First Degree Connections for {}", userId);
        return personRepository.getFirstDegreeConnections(userId);
    }
}
