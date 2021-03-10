package com.demo.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import com.demo.domain.enumeration.PStatus;

/**
 * A DTO for the {@link com.demo.domain.PersonStatus} entity.
 */
public class PersonStatusDTO implements Serializable {
    
    private Long id;

    @NotNull
    private PStatus code;

    @NotNull
    private String title;

    private String description;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PStatus getCode() {
        return code;
    }

    public void setCode(PStatus code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonStatusDTO)) {
            return false;
        }

        return id != null && id.equals(((PersonStatusDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonStatusDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
