package com.demo.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.demo.domain.Tag} entity.
 */
public class TagDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String name;


    private Long groupId;
    
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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TagDTO)) {
            return false;
        }

        return id != null && id.equals(((TagDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TagDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", groupId=" + getGroupId() +
            "}";
    }
}
