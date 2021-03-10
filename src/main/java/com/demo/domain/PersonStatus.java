package com.demo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.demo.domain.enumeration.PStatus;

/**
 * A PersonStatus.
 */
@Entity
@Table(name = "person_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PersonStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Enumerated(EnumType.STRING)
    private PStatus code;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public PStatus getCode() {
        return code;
    }

    public PersonStatus code(PStatus code) {
        this.code = code;
        return this;
    }

    public void setCode(PStatus code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public PersonStatus title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public PersonStatus description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonStatus)) {
            return false;
        }
        return code != null && code.equals(((PersonStatus) o).code);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonStatus{" +
            ", code='" + getCode() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
