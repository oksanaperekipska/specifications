package com.demo.service.dto;

import com.demo.domain.enumeration.GroupType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
public class GroupCustomDTO implements Serializable {

    private Long id;

    private String name;

    private GroupType type;

    private Boolean notification;

    private String superAdminName;

    private String superAdminPhone;

    private String tags;

    private Instant createdAt;
}
