package com.demo.service.filter;

import com.demo.domain.*;
import com.demo.repository.GroupRepository;
import com.demo.service.dto.GroupCustomDTO;
import com.demo.service.dto.PersonDTO;
import com.demo.service.filter.criteria.GroupCustomCriteria;
import com.demo.service.filter.criteria.PersonCriteria;
import com.demo.service.mapper.GroupMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Service for executing complex queries for {@link Person} entities in the database.
 * The main input is a {@link PersonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PersonDTO} or a {@link Page} of {@link PersonDTO} which fulfills the criteria.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupCustomQueryService extends CustomQueryService<Group> {

    private final Logger log = LoggerFactory.getLogger(GroupCustomQueryService.class);

    private final GroupRepository groupRepository;

    private final GroupMapper groupMapper;

    @Transactional(readOnly = true)
    public Page<GroupCustomDTO> findByCriteria(GroupCustomCriteria criteria, Pageable pageable) {
        log.debug("find by criteria : {}", criteria);

        final Specification<Group> specification = createSpecification(criteria);

        var poorGroupPage = groupRepository.findAll(specification, pageable);
        var ids = poorGroupPage.getContent().stream()
            .map(Group::getId)
            .collect(toList());

        var groupsPage = groupRepository.findCustomById(ids, pageable.getSort());

        return new PageImpl<>(groupsPage, pageable, poorGroupPage.getTotalElements())
            .map(groupMapper::toCustomDto);
    }

    protected Specification<Group> createSpecification(GroupCustomCriteria criteria) {
        Specification<Group> specification = Specification.where(null);

        specification = andEqualsFilter(specification, Group_.id, criteria.getIdEquals());
        specification = andContainsFilter(specification, Group_.name, criteria.getNameContains());
        specification = andInFilter(specification, Group_.type, criteria.getTypeIn());
        specification = andEqualsFilter(specification, Group_.notification, criteria.getNotificationEquals());

        specification = andContainsFilter(specification, root -> join(root, Group_.superAdmin, JoinType.LEFT).get(Person_.NAME), criteria.getSuperAdminNameContains());
        specification = andContainsFilter(specification, root -> join(root, Group_.superAdmin, JoinType.LEFT).get(Person_.PHONE), criteria.getSuperAdminPhoneContains());

        specification = andInFilter(specification, root -> join(root, Group_.tags, JoinType.LEFT).get(Tag_.NAME), criteria.getTagsIn());
        specification = andRangeFilter(specification, Group_.createdAt, criteria.getCreatedAtRange());

        return specification;
    }

    @Transactional(readOnly = true)
    public Page<GroupCustomDTO> findByCriteriaBad(GroupCustomCriteria criteria, Pageable pageable) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Group> specification = createBadSpecification(criteria);
        return groupRepository.findAll(specification, pageable)
            .map(groupMapper::toCustomDto);
    }

    protected Specification<Group> createBadSpecification(GroupCustomCriteria criteria) {
        Specification<Group> specification = Specification.where(null);

        if (criteria.getIdEquals() != null)
            specification = specification.and((root, query, builder) ->
                builder.equal(root.get(Group_.id), criteria.getIdEquals())
            );

        if (criteria.getNameContains() != null)
            specification = specification.and((root, query, builder) ->
                builder.like(
                    builder.upper(root.get(Group_.name)),
                    wrapLikeQuery(criteria.getNameContains()))
            );

        if (criteria.getTypeIn() != null)
            specification = specification.and((root, query, builder) ->
                root.get(Group_.type).in(criteria.getTypeIn())
            );

        if (criteria.getNotificationEquals() != null)
            specification = specification.and((root, query, builder) ->
                builder.equal(root.get(Group_.notification), criteria.getNotificationEquals())
            );

        // Filter by joined entity can produce duplicated joins
        if (criteria.getSuperAdminNameContains() != null)
            specification = specification.and((root, query, builder) ->
                builder.like(
                    builder.upper(root.join(Group_.superAdmin, JoinType.LEFT).get(Person_.name)),
                    wrapLikeQuery(criteria.getSuperAdminNameContains()))
            );

        if (criteria.getSuperAdminNameContains() != null)
            specification = specification.and((root, query, builder) ->
                builder.like(
                    builder.upper(root.join(Group_.superAdmin, JoinType.LEFT).get(Person_.phone)),
                    wrapLikeQuery(criteria.getSuperAdminPhoneContains()))
            );

        // Fetch paginated query is bad idea:
        // 1. if query has pagination or sort - throws exception on hibernate.query.fail_on_pagination_over_collection_fetch: true
        // 2. fetch apply only if filter apply
        if (criteria.getTagsIn() != null)
            specification = specification.and((root, query, builder) ->
                root.join(Group_.tags, JoinType.LEFT).get(Tag_.name).in(criteria.getTagsIn())
            );

        if (criteria.getCreatedAtRange() != null) {
            if (criteria.getCreatedAtRange().getGreaterThanOrEqual() != null)
                specification = specification.and((root, query, builder) ->
                    builder.greaterThanOrEqualTo(root.get(Group_.createdAt), criteria.getCreatedAtRange().getGreaterThanOrEqual())
                );

            if (criteria.getCreatedAtRange().getLessThanOrEqual() != null)
                specification = specification.and((root, query, builder) ->
                    builder.lessThanOrEqualTo(root.get(Group_.createdAt), criteria.getCreatedAtRange().getLessThanOrEqual())
                );
        }

        return specification;
    }
}
