package com.demo.service.filter.criteria;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.demo.domain.enumeration.PersonStatus;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.demo.domain.Person} entity. This class is used
 * in {@link com.demo.web.rest.PersonResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /people?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PersonCriteria implements Serializable, Criteria {
    /**
     * Class for filtering PersonStatus
     */
    public static class PersonStatusFilter extends Filter<PersonStatus> {

        public PersonStatusFilter() {
        }

        public PersonStatusFilter(PersonStatusFilter filter) {
            super(filter);
        }

        @Override
        public PersonStatusFilter copy() {
            return new PersonStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter username;

    private StringFilter phone;

    private PersonStatusFilter status;

    private InstantFilter lastActiveAt;

    private LongFilter groupId;

    public PersonCriteria() {
    }

    public PersonCriteria(PersonCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.username = other.username == null ? null : other.username.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.lastActiveAt = other.lastActiveAt == null ? null : other.lastActiveAt.copy();
        this.groupId = other.groupId == null ? null : other.groupId.copy();
    }

    @Override
    public PersonCriteria copy() {
        return new PersonCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getUsername() {
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public PersonStatusFilter getStatus() {
        return status;
    }

    public void setStatus(PersonStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(InstantFilter lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public LongFilter getGroupId() {
        return groupId;
    }

    public void setGroupId(LongFilter groupId) {
        this.groupId = groupId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PersonCriteria that = (PersonCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(username, that.username) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(status, that.status) &&
            Objects.equals(lastActiveAt, that.lastActiveAt) &&
            Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        username,
        phone,
        status,
        lastActiveAt,
        groupId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (username != null ? "username=" + username + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (lastActiveAt != null ? "lastActiveAt=" + lastActiveAt + ", " : "") +
                (groupId != null ? "groupId=" + groupId + ", " : "") +
            "}";
    }

}
