package com.demo.service;

import com.demo.domain.Group;
import com.demo.repository.GroupRepository;
import com.demo.service.dto.GroupDTO;
import com.demo.service.mapper.GroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Group}.
 */
@Service
@Transactional
public class GroupService {

    private final Logger log = LoggerFactory.getLogger(GroupService.class);

    private final GroupRepository groupRepository;

    private final GroupMapper groupMapper;

    public GroupService(GroupRepository groupRepository, GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
    }

    /**
     * Save a group.
     *
     * @param groupDTO the entity to save.
     * @return the persisted entity.
     */
    public GroupDTO save(GroupDTO groupDTO) {
        log.debug("Request to save Group : {}", groupDTO);
        Group group = groupMapper.toEntity(groupDTO);
        group = groupRepository.save(group);
        return groupMapper.toDto(group);
    }

    /**
     * Get all the groups.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GroupDTO> findAll() {
        log.debug("Request to get all Groups");
        return groupRepository.findAllWithEagerRelationships().stream()
            .map(groupMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get all the groups with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<GroupDTO> findAllWithEagerRelationships(Pageable pageable) {
        return groupRepository.findAllWithEagerRelationships(pageable).map(groupMapper::toDto);
    }

    /**
     * Get one group by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GroupDTO> findOne(Long id) {
        log.debug("Request to get Group : {}", id);
        return groupRepository.findOneWithEagerRelationships(id)
            .map(groupMapper::toDto);
    }

    /**
     * Delete the group by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Group : {}", id);
        groupRepository.deleteById(id);
    }
}
