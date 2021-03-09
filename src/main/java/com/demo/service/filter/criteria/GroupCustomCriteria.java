package com.demo.service.filter.criteria;

import com.demo.domain.enumeration.GroupType;
import com.demo.service.dto.PersonDTO;
import com.demo.service.filter.criteria.base.InstantFilter;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class GroupCustomCriteria implements Serializable {

    private Long idEquals;

    private String nameContains;

    private List<GroupType> typeIn;

    private Boolean notificationEquals;

    private String superAdminNameContains;

    private String superAdminPhoneContains;

    private List<String> tagsIn;

    private InstantFilter createdAtRange;
}
