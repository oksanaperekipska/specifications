package com.demo.service.filter;

import com.demo.service.filter.criteria.base.RangeFilter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class CustomQueryService<E> {

    protected <A extends Comparable<? super A>> Specification<E> andRangeFilter(Specification<E> specification,
                                                                                SingularAttribute<E, A> attr,
                                                                                RangeFilter<A> rangeFilter) {
        if (rangeFilter == null) return specification;

        if (rangeFilter.getGreaterThanOrEqual() != null)
            specification = specification.and((root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get(attr), rangeFilter.getGreaterThanOrEqual()));
        if (rangeFilter.getLessThanOrEqual() != null)
            specification = specification.and((root, query, builder) ->
                builder.lessThanOrEqualTo(root.get(attr), rangeFilter.getLessThanOrEqual()));

        return specification;
    }

    protected <A extends Comparable<? super A>> Specification<E> andEqualsFilter(Specification<E> specification,
                                                                                 SingularAttribute<E, A> attr,
                                                                                 A filter) {
        if (filter == null) return specification;

        return specification.and((root, query, builder) -> eq(attr, filter, root, builder));
    }

    protected <A extends Comparable<? super A>> Predicate eq(SingularAttribute<E, A> attr, A filter, Root<E> root, CriteriaBuilder builder) {
        return builder.equal(root.get(attr), filter);
    }

    protected <A extends Comparable<? super A>> Specification<E> andInFilter(Specification<E> specification,
                                                                             SingularAttribute<E, A> attr,
                                                                             Collection<A> filter) {
        if (filter == null) return specification;

        specification = specification.and((root, query, builder) ->
            in(r -> r.get(attr), filter, root, builder));

        return specification;
    }

    protected <A extends Comparable<? super A>> Specification<E> andInFilter(Specification<E> specification,
                                                                             Function<Root<E>, Expression<A>> attrFunc,
                                                                             Collection<A> filter) {
        if (filter == null) return specification;

        specification = specification.and((root, query, builder) ->
            in(attrFunc, filter, root, builder));

        return specification;
    }

    private <A extends Comparable<? super A>> CriteriaBuilder.In<A> in(Function<Root<E>, Expression<A>> attrFunc, Collection<A> filter, Root<E> root, CriteriaBuilder builder) {
        var in = builder.in(attrFunc.apply(root));
        filter.forEach(in::value);
        return in;
    }

    protected Specification<E> andContainsFilter(Specification<E> specification,
                                                 SingularAttribute<E, String> attr,
                                                 String stringFilter) {
        if (stringFilter == null) return specification;

        specification = specification.and((root, query, builder) ->
            contains(root.get(attr), stringFilter, builder));

        return specification;
    }

    protected Specification<E> andContainsFilter(Specification<E> specification,
                                                 Function<Root<E>, Expression<String>> attrFunc,
                                                 String stringFilter) {
        if (stringFilter == null) return specification;

        specification = specification.and((root, query, builder) ->
            contains(attrFunc.apply(root), stringFilter, builder));

        return specification;
    }

    protected Predicate contains(Expression<String> path, String stringFilter, CriteriaBuilder builder) {
        return builder.like(
            builder.upper(path),
            wrapLikeQuery(stringFilter));
    }

    protected Specification<E> andContainsWordFilter(Specification<E> specification,
                                                     SingularAttribute<E, String> attr,
                                                     String wordFilter) {
        if (wordFilter == null) return specification;

        specification = specification.and((root, query, builder) ->
            containsWord(root.get(attr), wordFilter, builder)
        );

        return specification;
    }

    protected Predicate containsWord(Path<String> path,
                                     String wordFilter,
                                     CriteriaBuilder builder) {
        return builder.like(
            builder.upper(path),
            wrapAsWordLikeQuery(wordFilter));
    }

    protected <F> Specification<E> andContainsAnyWordFilter(Specification<E> specification,
                                                            SingularAttribute<E, String> attr,
                                                            Collection<F> wordsFilter) {
        if (wordsFilter == null) return specification;

        specification = specification.and((root, query, builder) -> {
            Predicate[] predicates = wordsFilter.stream()
                .map(wordFilter -> containsWord(root.get(attr), wordFilter.toString(), builder))
                .toArray(Predicate[]::new);

            return builder.or(predicates);
        });

        return specification;
    }

    protected String wrapLikeQuery(String txt) {
        return "%" + txt.toUpperCase() + "%";
    }

    protected String wrapAsWordLikeQuery(String txt) {
        return "% " + txt.toUpperCase() + " %";
    }

    public <J> Join<E, J> join(Root<E> root,
                               Attribute<E, J> attr,
                               JoinType joinType) {
        return (Join<E, J>) root.getJoins().stream()
            .filter(j -> j.getAttribute().getName().equals(attr.getName()) && j.getJoinType().equals(joinType))
            .findFirst()
            .orElseGet(() -> root.join(attr.getName(), joinType));
    }
}
