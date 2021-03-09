package com.demo.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import com.demo.domain.enumeration.PersonStatus;

/**
 * A DTO for the {@link com.demo.domain.Person} entity.
 */
public class PersonDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String name;

    private String username;

    private String phone;

    @NotNull
    private PersonStatus status;

    @NotNull
    private Instant lastActiveAt;

    
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public PersonStatus getStatus() {
        return status;
    }

    public void setStatus(PersonStatus status) {
        this.status = status;
    }

    public Instant getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonDTO)) {
            return false;
        }

        return id != null && id.equals(((PersonDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", username='" + getUsername() + "'" +
            ", phone='" + getPhone() + "'" +
            ", status='" + getStatus() + "'" +
            ", lastActiveAt='" + getLastActiveAt() + "'" +
            "}";
    }
}
