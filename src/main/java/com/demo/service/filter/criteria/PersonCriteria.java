package com.demo.service.filter.criteria;

import java.io.Serializable;
import java.util.Objects;

import com.demo.domain.enumeration.PStatus;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;
import lombok.Data;

/**
 * Criteria class for the {@link com.demo.domain.Person} entity. This class is used
 * in {@link com.demo.web.rest.PersonResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /people?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
public class PersonCriteria implements Serializable {
    /**
     * Class for filtering PStatus
     */
    public static class PStatusFilter extends Filter<PStatus> {

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter username;

    private StringFilter phone;

    private InstantFilter lastActiveAt;

    private PStatusFilter status;

    private LongFilter groupId;

}
