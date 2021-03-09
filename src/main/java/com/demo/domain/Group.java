package com.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.demo.domain.enumeration.GroupType;

/**
 * A Group.
 */
@Entity
@Table(name = "jhi_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private GroupType type;

    @Column(name = "notification")
    private Boolean notification;

    @OneToMany(mappedBy = "group")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "groups", allowSetters = true)
    private Person superAdmin;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "jhi_group_members",
               joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "members_id", referencedColumnName = "id"))
    private Set<Person> members = new HashSet<>();

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

    public Group name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupType getType() {
        return type;
    }

    public Group type(GroupType type) {
        this.type = type;
        return this;
    }

    public void setType(GroupType type) {
        this.type = type;
    }

    public Boolean isNotification() {
        return notification;
    }

    public Group notification(Boolean notification) {
        this.notification = notification;
        return this;
    }

    public void setNotification(Boolean notification) {
        this.notification = notification;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Group tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Group addTags(Tag tag) {
        this.tags.add(tag);
        tag.setGroup(this);
        return this;
    }

    public Group removeTags(Tag tag) {
        this.tags.remove(tag);
        tag.setGroup(null);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Person getSuperAdmin() {
        return superAdmin;
    }

    public Group superAdmin(Person person) {
        this.superAdmin = person;
        return this;
    }

    public void setSuperAdmin(Person person) {
        this.superAdmin = person;
    }

    public Set<Person> getMembers() {
        return members;
    }

    public Group members(Set<Person> people) {
        this.members = people;
        return this;
    }

    public Group addMembers(Person person) {
        this.members.add(person);
        person.getGroups().add(this);
        return this;
    }

    public Group removeMembers(Person person) {
        this.members.remove(person);
        person.getGroups().remove(this);
        return this;
    }

    public void setMembers(Set<Person> people) {
        this.members = people;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Group)) {
            return false;
        }
        return id != null && id.equals(((Group) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Group{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", notification='" + isNotification() + "'" +
            "}";
    }
}
