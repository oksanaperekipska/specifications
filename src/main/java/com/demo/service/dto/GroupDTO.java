package com.demo.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import com.demo.domain.enumeration.GroupType;

/**
 * A DTO for the {@link com.demo.domain.Group} entity.
 */
public class GroupDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private GroupType type;

    private Boolean notification;


    private Long superAdminId;

    private Set<PersonDTO> members = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) {
        this.type = type;
    }

    public Boolean isNotification() {
        return notification;
    }

    public void setNotification(Boolean notification) {
        this.notification = notification;
    }

    public Long getSuperAdminId() {
        return superAdminId;
    }

    public void setSuperAdminId(Long personId) {
        this.superAdminId = personId;
    }

    public Set<PersonDTO> getMembers() {
        return members;
    }

    public void setMembers(Set<PersonDTO> people) {
        this.members = people;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupDTO)) {
            return false;
        }

        return id != null && id.equals(((GroupDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GroupDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", notification='" + isNotification() + "'" +
            ", superAdminId=" + getSuperAdminId() +
            ", members='" + getMembers() + "'" +
            "}";
    }
}
