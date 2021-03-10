package com.demo.web.rest;

import com.demo.domain.enumeration.PStatus;
import com.demo.service.PersonStatusService;
import com.demo.web.rest.errors.BadRequestAlertException;
import com.demo.service.dto.PersonStatusDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.demo.domain.PersonStatus}.
 */
@RestController
@RequestMapping("/api")
public class PersonStatusResource {

    private final Logger log = LoggerFactory.getLogger(PersonStatusResource.class);

    private static final String ENTITY_NAME = "personStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonStatusService personStatusService;

    public PersonStatusResource(PersonStatusService personStatusService) {
        this.personStatusService = personStatusService;
    }

    /**
     * {@code GET  /person-statuses} : get all the personStatuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personStatuses in body.
     */
    @GetMapping("/person-statuses")
    public List<PersonStatusDTO> getAllPersonStatuses() {
        log.debug("REST request to get all PersonStatuses");
        return personStatusService.findAll();
    }

    /**
     * {@code GET  /person-statuses/:id} : get the "id" personStatus.
     *
     * @param id the id of the personStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/person-statuses/{id}")
    public ResponseEntity<PersonStatusDTO> getPersonStatus(@PathVariable PStatus id) {
        log.debug("REST request to get PersonStatus : {}", id);
        Optional<PersonStatusDTO> personStatusDTO = personStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personStatusDTO);
    }
}
