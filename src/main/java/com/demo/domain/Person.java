package com.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Person.
 */
@Entity
@Table(name = "person")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "phone")
    private String phone;

    @NotNull
    @Column(name = "last_active_at", nullable = false)
    private Instant lastActiveAt;

    @NotNull
    @ManyToOne
    @JsonIgnoreProperties(value = "people", allowSetters = true)
    private PersonStatus status;

    @ManyToMany(mappedBy = "members")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Group> groups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Person name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public Person username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public Person phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Instant getLastActiveAt() {
        return lastActiveAt;
    }

    public Person lastActiveAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
        return this;
    }

    public void setLastActiveAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public PersonStatus getStatus() {
        return status;
    }

    public Person status(PersonStatus personStatus) {
        this.status = personStatus;
        return this;
    }

    public void setStatus(PersonStatus personStatus) {
        this.status = personStatus;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public Person groups(Set<Group> groups) {
        this.groups = groups;
        return this;
    }

    public Person addGroup(Group group) {
        this.groups.add(group);
        group.getMembers().add(this);
        return this;
    }

    public Person removeGroup(Group group) {
        this.groups.remove(group);
        group.getMembers().remove(this);
        return this;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Person{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", username='" + getUsername() + "'" +
            ", phone='" + getPhone() + "'" +
            ", lastActiveAt='" + getLastActiveAt() + "'" +
            "}";
    }
}
