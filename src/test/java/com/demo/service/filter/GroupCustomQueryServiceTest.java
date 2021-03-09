package com.demo.service.filter;

import com.demo.SpecificationsApp;
import com.demo.config.TestSecurityConfiguration;
import com.demo.domain.Group;
import com.demo.domain.Group_;
import com.demo.domain.Person_;
import com.demo.service.dto.GroupCustomDTO;
import com.demo.service.filter.criteria.GroupCustomCriteria;
import com.demo.service.mapper.GroupMapper;
import com.google.common.base.Strings;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = {SpecificationsApp.class, TestSecurityConfiguration.class})
public class GroupCustomQueryServiceTest {

    @Autowired
    private GroupCustomQueryService groupCustomQueryService;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private EntityManager em;

    @BeforeEach
    @Transactional
    public void setUp() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Group> query = builder.createQuery(Group.class);
        Root<Group> root = query.from(Group.class);

        Predicate predicate1 = builder.greaterThan(root.get(Group_.createdAt), Instant.now().minus(1, ChronoUnit.YEARS));
        Predicate predicate2 = builder.lessThan(root.get(Group_.createdAt), Instant.now());

        EntityGraph<Group> entityGraph = em.createEntityGraph(Group.class);
        entityGraph.addAttributeNodes("superAdmin", "tags");

        List<Group> allGroups = em.createQuery(query.select(root).where(builder.and(predicate1, predicate2)))
            .setHint("javax.persistence.fetchgraph", entityGraph)
            .getResultList();

        printResult(new PageImpl<>(allGroups.stream().map(groupMapper::toCustomDto).collect(Collectors.toList()),
            Pageable.unpaged(),
            allGroups.size()
        ));
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(Strings.repeat("=", 138));
        System.out.printf("%60s%n", "TEST RESULT");
        System.out.println(Strings.repeat("=", 138));
    }

    @Test
    @Transactional
    public void findByCriteriaBad() {
        // Given
        var criteria = GroupCustomCriteria.builder()
//            .idEquals(1L)
//            .nameContains("")
//            .typeIn(List.of(GroupType.PUBLIC))
//            .notificationEquals(false)
//            .createdAtRange(new InstantFilter(Instant.parse("2021-03-08T00:00:00"), Instant.parse("2021-03-09T00:00:00")))
            .superAdminNameContains("а")
            .superAdminPhoneContains("05")
            .tagsIn(List.of("cat", "news", "needlework"))
            .build();

        var pageable = PageRequest.of(0, 2);
//            Pageable.unpaged();

        // When
        var groups = groupCustomQueryService.findByCriteriaBad(criteria, pageable);

        // Then
        printResult(groups);
    }

    @Test
    @Transactional
    public void findByCriteria() {
        // Given
        var criteria = GroupCustomCriteria.builder()
//            .idEquals(1L)
//            .nameContains("New")
//            .typeIn(List.of(GroupType.PUBLIC))
//            .notificationEquals(false)
//            .createdAtRange(new InstantFilter(Instant.parse("2021-03-08T00:00:00.000Z"), Instant.parse("2021-03-09T00:00:00.000Z")))
            .superAdminNameContains("а")
            .superAdminPhoneContains("05")
            .tagsIn(List.of("cat", "news", "needlework"))
            .build();

        var pageable = PageRequest.of(0, 2);

        // When
        var groups = groupCustomQueryService.findByCriteria(criteria, pageable);

        // Then
        printResult(groups);
    }

    @Test
    @Transactional
    public void findByCriteria_sort() {
        // Given
        var criteria = GroupCustomCriteria.builder().build();

        var pageable = PageRequest.of(0, 2,
            Sort.Direction.DESC, Group_.CREATED_AT
        );

        // When
        var groups = groupCustomQueryService.findByCriteria(criteria, pageable);

        // Then
        printResult(groups);
    }

    private void printResult(Page<GroupCustomDTO> groups) {
        printLine();
        printHeader();
        printLine();
        groups.forEach(groupDTO -> {
            System.out.printf(getColumnsString(),
                groupDTO.getId(),
                groupDTO.getName(),
                groupDTO.getType(),
                groupDTO.getNotification(),
                groupDTO.getSuperAdminName(),
                groupDTO.getSuperAdminPhone(),
                groupDTO.getCreatedAt(),
                groupDTO.getTags());
            printLine();
        });

        System.out.printf("Page: %s of %s%nTotal elements: %s", groups.getNumber() + 1, groups.getTotalPages(), groups.getTotalElements());
    }

    @NotNull
    private String getColumnsString() {
        return "|%4s|%20s|%10s|%15s|%20s|%20s|%20s|%20s|%n";
    }

    private void printHeader() {
        System.out.printf(getColumnsString(),
            Group_.ID,
            Group_.NAME,
            Group_.TYPE,
            Group_.NOTIFICATION,
            Group_.SUPER_ADMIN + "." + Person_.NAME,
            Group_.SUPER_ADMIN + "." + Person_.PHONE,
            Group_.CREATED_AT,
            Group_.TAGS);
    }

    private void printLine() {
        System.out.println("|" + Strings.repeat("-", 136) + "|");
    }
}
