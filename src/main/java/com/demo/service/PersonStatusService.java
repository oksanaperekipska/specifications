package com.demo.service;

import com.demo.domain.PersonStatus;
import com.demo.domain.enumeration.PStatus;
import com.demo.repository.PersonStatusRepository;
import com.demo.service.dto.PersonStatusDTO;
import com.demo.service.mapper.PersonStatusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link PersonStatus}.
 */
@Service
@Transactional
public class PersonStatusService {

    private final Logger log = LoggerFactory.getLogger(PersonStatusService.class);

    private final PersonStatusRepository personStatusRepository;

    private final PersonStatusMapper personStatusMapper;

    public PersonStatusService(PersonStatusRepository personStatusRepository, PersonStatusMapper personStatusMapper) {
        this.personStatusRepository = personStatusRepository;
        this.personStatusMapper = personStatusMapper;
    }

    /**
     * Get all the personStatuses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PersonStatusDTO> findAll() {
        log.debug("Request to get all PersonStatuses");
        return personStatusRepository.findAll().stream()
            .map(personStatusMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one personStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonStatusDTO> findOne(PStatus id) {
        log.debug("Request to get PersonStatus : {}", id);
        return personStatusRepository.findById(id)
            .map(personStatusMapper::toDto);
    }
}
