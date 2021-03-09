package com.demo.service.mapper;


import com.demo.domain.*;
import com.demo.service.dto.GroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Group} and its DTO {@link GroupDTO}.
 */
@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface GroupMapper extends EntityMapper<GroupDTO, Group> {

    @Mapping(source = "superAdmin.id", target = "superAdminId")
    GroupDTO toDto(Group group);

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "removeTags", ignore = true)
    @Mapping(source = "superAdminId", target = "superAdmin")
    @Mapping(target = "removeMembers", ignore = true)
    Group toEntity(GroupDTO groupDTO);

    default Group fromId(Long id) {
        if (id == null) {
            return null;
        }
        Group group = new Group();
        group.setId(id);
        return group;
    }
}
