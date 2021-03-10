package com.demo.web.rest;

import com.demo.SpecificationsApp;
import com.demo.config.TestSecurityConfiguration;
import com.demo.domain.PersonStatus;
import com.demo.repository.PersonStatusRepository;
import com.demo.service.PersonStatusService;
import com.demo.service.dto.PersonStatusDTO;
import com.demo.service.mapper.PersonStatusMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.demo.domain.enumeration.PStatus;
/**
 * Integration tests for the {@link PersonStatusResource} REST controller.
 */
@SpringBootTest(classes = { SpecificationsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class PersonStatusResourceIT {

    private static final PStatus DEFAULT_CODE = PStatus.ONLINE;
    private static final PStatus UPDATED_CODE = PStatus.BUSY;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private PersonStatusRepository personStatusRepository;

    @Autowired
    private PersonStatusMapper personStatusMapper;

    @Autowired
    private PersonStatusService personStatusService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonStatusMockMvc;

    private PersonStatus personStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonStatus createEntity(EntityManager em) {
        PersonStatus personStatus = new PersonStatus()
            .code(DEFAULT_CODE)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION);
        return personStatus;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonStatus createUpdatedEntity(EntityManager em) {
        PersonStatus personStatus = new PersonStatus()
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION);
        return personStatus;
    }

    @BeforeEach
    public void initTest() {
        personStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllPersonStatuses() throws Exception {
        // Initialize the database
        personStatusRepository.saveAndFlush(personStatus);

        // Get all the personStatusList
        restPersonStatusMockMvc.perform(get("/api/person-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void getPersonStatus() throws Exception {
        // Initialize the database
        personStatusRepository.saveAndFlush(personStatus);

        // Get the personStatus
        restPersonStatusMockMvc.perform(get("/api/person-statuses/{id}", personStatus.getCode()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingPersonStatus() throws Exception {
        // Get the personStatus
        em.remove(em.find(PersonStatus.class, DEFAULT_CODE));
        restPersonStatusMockMvc.perform(get("/api/person-statuses/{id}", DEFAULT_CODE))
            .andExpect(status().isNotFound());
    }
}
