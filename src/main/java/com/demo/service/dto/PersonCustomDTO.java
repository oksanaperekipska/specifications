package com.demo.service.dto;

import com.demo.domain.enumeration.PersonStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
public class PersonCustomDTO implements Serializable {

    private Long id;

    private String name;

    private String username;

    private String phone;

    private PersonStatus status;

    private Instant lastActiveAt;

    private List<GroupDTO> groups;
}
