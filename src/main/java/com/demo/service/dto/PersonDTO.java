package com.demo.service.dto;

import com.demo.domain.enumeration.PStatus;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;

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
    private PStatus status;

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

    public Instant getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public PStatus getStatus() {
        return status;
    }

    public void setStatus(PStatus status) {
        this.status = status;
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
            ", lastActiveAt='" + getLastActiveAt() + "'" +
            "}";
    }
}
