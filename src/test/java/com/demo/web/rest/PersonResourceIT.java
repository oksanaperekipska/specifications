package com.demo.web.rest;

import com.demo.SpecificationsApp;
import com.demo.config.TestSecurityConfiguration;
import com.demo.domain.Group;
import com.demo.domain.Person;
import com.demo.domain.PersonStatus;
import com.demo.domain.Tag;
import com.demo.domain.enumeration.PStatus;
import com.demo.repository.PersonRepository;
import com.demo.service.PersonService;
import com.demo.service.dto.PersonDTO;
import com.demo.service.filter.PersonQueryService;
import com.demo.service.mapper.PersonMapper;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PersonResource} REST controller.
 */
@SpringBootTest(classes = { SpecificationsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class PersonResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_ACTIVE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_ACTIVE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final PStatus DEFAULT_STATUS = PStatus.ONLINE;
    private static final PStatus UPDATED_STATUS = PStatus.BUSY;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonQueryService personQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonMockMvc;

    private Person person;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Person createEntity(EntityManager em) {
        PersonStatus status = em.find(PersonStatus.class, DEFAULT_STATUS);
        Person person = new Person()
            .status(status)
            .name(DEFAULT_NAME)
            .username(DEFAULT_USERNAME)
            .phone(DEFAULT_PHONE)
            .lastActiveAt(DEFAULT_LAST_ACTIVE_AT);
        return person;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Person createUpdatedEntity(EntityManager em) {
        PersonStatus status = em.find(PersonStatus.class, UPDATED_STATUS);
        Person person = new Person()
            .status(status)
            .name(UPDATED_NAME)
            .username(UPDATED_USERNAME)
            .phone(UPDATED_PHONE)
            .lastActiveAt(UPDATED_LAST_ACTIVE_AT);
        return person;
    }

    @BeforeEach
    public void initTest() {
        TestUtil.findAll(em, Tag.class).forEach(it -> em.remove(it));
        TestUtil.findAll(em, Group.class).forEach(it -> em.remove(it));
        TestUtil.findAll(em, Person.class).forEach(it -> em.remove(it));
        person = createEntity(em);
    }

    @Test
    @Transactional
    public void createPerson() throws Exception {
        int databaseSizeBeforeCreate = personRepository.findAll().size();
        // Create the Person
        PersonDTO personDTO = personMapper.toDto(person);
        restPersonMockMvc.perform(post("/api/people").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isCreated());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate + 1);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getStatus().getCode()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPerson.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPerson.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testPerson.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testPerson.getLastActiveAt()).isEqualTo(DEFAULT_LAST_ACTIVE_AT);
    }

    @Test
    @Transactional
    public void createPersonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = personRepository.findAll().size();

        // Create the Person with an existing ID
        person.setId(1L);
        PersonDTO personDTO = personMapper.toDto(person);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonMockMvc.perform(post("/api/people").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = personRepository.findAll().size();
        // set the field null
        person.setName(null);

        // Create the Person, which fails.
        PersonDTO personDTO = personMapper.toDto(person);


        restPersonMockMvc.perform(post("/api/people").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isBadRequest());

        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastActiveAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = personRepository.findAll().size();
        // set the field null
        person.setLastActiveAt(null);

        // Create the Person, which fails.
        PersonDTO personDTO = personMapper.toDto(person);


        restPersonMockMvc.perform(post("/api/people").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isBadRequest());

        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPeople() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList
        restPersonMockMvc.perform(get("/api/people?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].lastActiveAt").value(hasItem(DEFAULT_LAST_ACTIVE_AT.toString())));
    }

    @Test
    @Transactional
    public void getPerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get the person
        restPersonMockMvc.perform(get("/api/people/{id}", person.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(person.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.lastActiveAt").value(DEFAULT_LAST_ACTIVE_AT.toString()));
    }


    @Test
    @Transactional
    public void getPeopleByIdFiltering() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        Long id = person.getId();

        defaultPersonShouldBeFound("id.equals=" + id);
        defaultPersonShouldNotBeFound("id.notEquals=" + id);

        defaultPersonShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPersonShouldNotBeFound("id.greaterThan=" + id);

        defaultPersonShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPersonShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPeopleByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where name equals to DEFAULT_NAME
        defaultPersonShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the personList where name equals to UPDATED_NAME
        defaultPersonShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where name not equals to DEFAULT_NAME
        defaultPersonShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the personList where name not equals to UPDATED_NAME
        defaultPersonShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByNameIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPersonShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the personList where name equals to UPDATED_NAME
        defaultPersonShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where name is not null
        defaultPersonShouldBeFound("name.specified=true");

        // Get all the personList where name is null
        defaultPersonShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleByNameContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where name contains DEFAULT_NAME
        defaultPersonShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the personList where name contains UPDATED_NAME
        defaultPersonShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByNameNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where name does not contain DEFAULT_NAME
        defaultPersonShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the personList where name does not contain UPDATED_NAME
        defaultPersonShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllPeopleByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where username equals to DEFAULT_USERNAME
        defaultPersonShouldBeFound("username.equals=" + DEFAULT_USERNAME);

        // Get all the personList where username equals to UPDATED_USERNAME
        defaultPersonShouldNotBeFound("username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByUsernameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where username not equals to DEFAULT_USERNAME
        defaultPersonShouldNotBeFound("username.notEquals=" + DEFAULT_USERNAME);

        // Get all the personList where username not equals to UPDATED_USERNAME
        defaultPersonShouldBeFound("username.notEquals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where username in DEFAULT_USERNAME or UPDATED_USERNAME
        defaultPersonShouldBeFound("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME);

        // Get all the personList where username equals to UPDATED_USERNAME
        defaultPersonShouldNotBeFound("username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where username is not null
        defaultPersonShouldBeFound("username.specified=true");

        // Get all the personList where username is null
        defaultPersonShouldNotBeFound("username.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleByUsernameContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where username contains DEFAULT_USERNAME
        defaultPersonShouldBeFound("username.contains=" + DEFAULT_USERNAME);

        // Get all the personList where username contains UPDATED_USERNAME
        defaultPersonShouldNotBeFound("username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where username does not contain DEFAULT_USERNAME
        defaultPersonShouldNotBeFound("username.doesNotContain=" + DEFAULT_USERNAME);

        // Get all the personList where username does not contain UPDATED_USERNAME
        defaultPersonShouldBeFound("username.doesNotContain=" + UPDATED_USERNAME);
    }


    @Test
    @Transactional
    public void getAllPeopleByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where phone equals to DEFAULT_PHONE
        defaultPersonShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the personList where phone equals to UPDATED_PHONE
        defaultPersonShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPeopleByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where phone not equals to DEFAULT_PHONE
        defaultPersonShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the personList where phone not equals to UPDATED_PHONE
        defaultPersonShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPeopleByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultPersonShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the personList where phone equals to UPDATED_PHONE
        defaultPersonShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPeopleByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where phone is not null
        defaultPersonShouldBeFound("phone.specified=true");

        // Get all the personList where phone is null
        defaultPersonShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleByPhoneContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where phone contains DEFAULT_PHONE
        defaultPersonShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the personList where phone contains UPDATED_PHONE
        defaultPersonShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPeopleByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where phone does not contain DEFAULT_PHONE
        defaultPersonShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the personList where phone does not contain UPDATED_PHONE
        defaultPersonShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllPeopleByLastActiveAtIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastActiveAt equals to DEFAULT_LAST_ACTIVE_AT
        defaultPersonShouldBeFound("lastActiveAt.equals=" + DEFAULT_LAST_ACTIVE_AT);

        // Get all the personList where lastActiveAt equals to UPDATED_LAST_ACTIVE_AT
        defaultPersonShouldNotBeFound("lastActiveAt.equals=" + UPDATED_LAST_ACTIVE_AT);
    }

    @Test
    @Transactional
    public void getAllPeopleByLastActiveAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastActiveAt not equals to DEFAULT_LAST_ACTIVE_AT
        defaultPersonShouldNotBeFound("lastActiveAt.notEquals=" + DEFAULT_LAST_ACTIVE_AT);

        // Get all the personList where lastActiveAt not equals to UPDATED_LAST_ACTIVE_AT
        defaultPersonShouldBeFound("lastActiveAt.notEquals=" + UPDATED_LAST_ACTIVE_AT);
    }

    @Test
    @Transactional
    public void getAllPeopleByLastActiveAtIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastActiveAt in DEFAULT_LAST_ACTIVE_AT or UPDATED_LAST_ACTIVE_AT
        defaultPersonShouldBeFound("lastActiveAt.in=" + DEFAULT_LAST_ACTIVE_AT + "," + UPDATED_LAST_ACTIVE_AT);

        // Get all the personList where lastActiveAt equals to UPDATED_LAST_ACTIVE_AT
        defaultPersonShouldNotBeFound("lastActiveAt.in=" + UPDATED_LAST_ACTIVE_AT);
    }

    @Test
    @Transactional
    public void getAllPeopleByLastActiveAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastActiveAt is not null
        defaultPersonShouldBeFound("lastActiveAt.specified=true");

        // Get all the personList where lastActiveAt is null
        defaultPersonShouldNotBeFound("lastActiveAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllPeopleByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        PStatus statusCode = person.getStatus().getCode();

        // Get all the personList where status equals to status
        defaultPersonShouldBeFound("status.equals=" + statusCode);

        // Get all the personList where status equals to status + 1
        defaultPersonShouldNotBeFound("status.equals=" + (UPDATED_STATUS));
    }


    @Test
    @Transactional
    public void getAllPeopleByGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        Group group = GroupResourceIT.createEntity(em);
        em.persist(group);
        em.flush();
        person.addGroup(group);
        personRepository.saveAndFlush(person);
        Long groupId = group.getId();

        // Get all the personList where group equals to groupId
        defaultPersonShouldBeFound("groupId.equals=" + groupId);

        // Get all the personList where group equals to groupId + 1
        defaultPersonShouldNotBeFound("groupId.equals=" + (groupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonShouldBeFound(String filter) throws Exception {
        restPersonMockMvc.perform(get("/api/people?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].lastActiveAt").value(hasItem(DEFAULT_LAST_ACTIVE_AT.toString())));

        // Check, that the count call also returns 1
        restPersonMockMvc.perform(get("/api/people/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonShouldNotBeFound(String filter) throws Exception {
        restPersonMockMvc.perform(get("/api/people?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonMockMvc.perform(get("/api/people/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPerson() throws Exception {
        // Get the person
        restPersonMockMvc.perform(get("/api/people/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Update the person
        Person updatedPerson = personRepository.findById(person.getId()).get();
        // Disconnect from session so that the updates on updatedPerson are not directly saved in db
        em.detach(updatedPerson);
        updatedPerson
            .status(em.find(PersonStatus.class, UPDATED_STATUS))
            .name(UPDATED_NAME)
            .username(UPDATED_USERNAME)
            .phone(UPDATED_PHONE)
            .lastActiveAt(UPDATED_LAST_ACTIVE_AT);
        PersonDTO personDTO = personMapper.toDto(updatedPerson);

        restPersonMockMvc.perform(put("/api/people").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isOk());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getStatus().getCode()).isEqualTo(UPDATED_STATUS);
        assertThat(testPerson.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPerson.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testPerson.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testPerson.getLastActiveAt()).isEqualTo(UPDATED_LAST_ACTIVE_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(person);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonMockMvc.perform(put("/api/people").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        int databaseSizeBeforeDelete = personRepository.findAll().size();

        // Delete the person
        restPersonMockMvc.perform(delete("/api/people/{id}", person.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
