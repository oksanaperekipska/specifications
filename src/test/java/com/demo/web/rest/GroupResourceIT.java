package com.demo.web.rest;

import com.demo.SpecificationsApp;
import com.demo.config.TestSecurityConfiguration;
import com.demo.domain.Group;
import com.demo.repository.GroupRepository;
import com.demo.service.GroupService;
import com.demo.service.dto.GroupDTO;
import com.demo.service.mapper.GroupMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.demo.domain.enumeration.GroupType;
/**
 * Integration tests for the {@link GroupResource} REST controller.
 */
@SpringBootTest(classes = { SpecificationsApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class GroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final GroupType DEFAULT_TYPE = GroupType.PRIVATE;
    private static final GroupType UPDATED_TYPE = GroupType.PUBLIC;

    private static final Boolean DEFAULT_NOTIFICATION = false;
    private static final Boolean UPDATED_NOTIFICATION = true;

    @Autowired
    private GroupRepository groupRepository;

    @Mock
    private GroupRepository groupRepositoryMock;

    @Autowired
    private GroupMapper groupMapper;

    @Mock
    private GroupService groupServiceMock;

    @Autowired
    private GroupService groupService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGroupMockMvc;

    private Group group;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Group createEntity(EntityManager em) {
        Group group = new Group()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .notification(DEFAULT_NOTIFICATION);
        return group;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Group createUpdatedEntity(EntityManager em) {
        Group group = new Group()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .notification(UPDATED_NOTIFICATION);
        return group;
    }

    @BeforeEach
    public void initTest() {
        group = createEntity(em);
    }

    @Test
    @Transactional
    public void createGroup() throws Exception {
        int databaseSizeBeforeCreate = groupRepository.findAll().size();
        // Create the Group
        GroupDTO groupDTO = groupMapper.toDto(group);
        restGroupMockMvc.perform(post("/api/groups").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(groupDTO)))
            .andExpect(status().isCreated());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeCreate + 1);
        Group testGroup = groupList.get(groupList.size() - 1);
        assertThat(testGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGroup.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testGroup.isNotification()).isEqualTo(DEFAULT_NOTIFICATION);
    }

    @Test
    @Transactional
    public void createGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = groupRepository.findAll().size();

        // Create the Group with an existing ID
        group.setId(1L);
        GroupDTO groupDTO = groupMapper.toDto(group);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGroupMockMvc.perform(post("/api/groups").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(groupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupRepository.findAll().size();
        // set the field null
        group.setName(null);

        // Create the Group, which fails.
        GroupDTO groupDTO = groupMapper.toDto(group);


        restGroupMockMvc.perform(post("/api/groups").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(groupDTO)))
            .andExpect(status().isBadRequest());

        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupRepository.findAll().size();
        // set the field null
        group.setType(null);

        // Create the Group, which fails.
        GroupDTO groupDTO = groupMapper.toDto(group);


        restGroupMockMvc.perform(post("/api/groups").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(groupDTO)))
            .andExpect(status().isBadRequest());

        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGroups() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList
        restGroupMockMvc.perform(get("/api/groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(group.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].notification").value(hasItem(DEFAULT_NOTIFICATION.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        when(groupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGroupMockMvc.perform(get("/api/groups?eagerload=true"))
            .andExpect(status().isOk());

        verify(groupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(groupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGroupMockMvc.perform(get("/api/groups?eagerload=true"))
            .andExpect(status().isOk());

        verify(groupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getGroup() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get the group
        restGroupMockMvc.perform(get("/api/groups/{id}", group.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(group.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.notification").value(DEFAULT_NOTIFICATION.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingGroup() throws Exception {
        // Get the group
        restGroupMockMvc.perform(get("/api/groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGroup() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        int databaseSizeBeforeUpdate = groupRepository.findAll().size();

        // Update the group
        Group updatedGroup = groupRepository.findById(group.getId()).get();
        // Disconnect from session so that the updates on updatedGroup are not directly saved in db
        em.detach(updatedGroup);
        updatedGroup
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .notification(UPDATED_NOTIFICATION);
        GroupDTO groupDTO = groupMapper.toDto(updatedGroup);

        restGroupMockMvc.perform(put("/api/groups").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(groupDTO)))
            .andExpect(status().isOk());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
        Group testGroup = groupList.get(groupList.size() - 1);
        assertThat(testGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGroup.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testGroup.isNotification()).isEqualTo(UPDATED_NOTIFICATION);
    }

    @Test
    @Transactional
    public void updateNonExistingGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().size();

        // Create the Group
        GroupDTO groupDTO = groupMapper.toDto(group);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupMockMvc.perform(put("/api/groups").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(groupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGroup() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        int databaseSizeBeforeDelete = groupRepository.findAll().size();

        // Delete the group
        restGroupMockMvc.perform(delete("/api/groups/{id}", group.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
