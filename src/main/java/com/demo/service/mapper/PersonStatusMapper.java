package com.demo.service.mapper;


import com.demo.domain.*;
import com.demo.domain.enumeration.PStatus;
import com.demo.service.dto.PersonStatusDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PersonStatus} and its DTO {@link PersonStatusDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PersonStatusMapper extends EntityMapper<PersonStatusDTO, PersonStatus> {



    default PersonStatus fromId(PStatus code) {
        if (code == null) {
            return null;
        }
        PersonStatus personStatus = new PersonStatus();
        personStatus.setCode(code);
        return personStatus;
    }
}
