package com.demo.service.mapper;


import com.demo.domain.*;
import com.demo.service.dto.TagDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring", uses = {GroupMapper.class})
public interface TagMapper extends EntityMapper<TagDTO, Tag> {

    @Mapping(source = "group.id", target = "groupId")
    TagDTO toDto(Tag tag);

    @Mapping(source = "groupId", target = "group")
    Tag toEntity(TagDTO tagDTO);

    default Tag fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tag tag = new Tag();
        tag.setId(id);
        return tag;
    }
}
