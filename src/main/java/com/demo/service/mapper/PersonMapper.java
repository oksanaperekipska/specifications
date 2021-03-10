package com.demo.service.mapper;


import com.demo.domain.*;
import com.demo.service.dto.PersonDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Person} and its DTO {@link PersonDTO}.
 */
@Mapper(componentModel = "spring", uses = {PersonStatusMapper.class})
public interface PersonMapper extends EntityMapper<PersonDTO, Person> {

    @Mapping(source = "status.code", target = "status")
    PersonDTO toDto(Person person);

    @Mapping(target = "groups", ignore = true)
    @Mapping(target = "removeGroup", ignore = true)
    Person toEntity(PersonDTO personDTO);

    default Person fromId(Long id) {
        if (id == null) {
            return null;
        }
        Person person = new Person();
        person.setId(id);
        return person;
    }
}
