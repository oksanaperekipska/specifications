package com.demo.service.filter;

import com.demo.SpecificationsApp;
import com.demo.config.TestSecurityConfiguration;
import com.demo.domain.Person_;
import com.demo.service.filter.criteria.PersonCriteria;
import com.demo.service.dto.PersonDTO;
import com.demo.service.filter.PersonQueryService;
import com.google.common.base.Strings;
import io.github.jhipster.service.filter.LongFilter;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

@SpringBootTest(classes = {SpecificationsApp.class, TestSecurityConfiguration.class})
public class PersonCustomQueryServiceTest {

    @Autowired
    private PersonQueryService personQueryService;

    @Autowired
    private EntityManager em;

    @Test
    public void findByCriteria() {
        // Given
        var criteria = new PersonCriteria();
        var groupsFilter = new LongFilter();
        groupsFilter.setEquals(1L);
        criteria.setGroupId(groupsFilter);

        var pageable = Pageable.unpaged();
        pageable = PageRequest.of(0, 2);

        // When
        var people = personQueryService.findByCriteria(criteria, pageable);

        // Then
        printResult(people);
    }

    private void printResult(Page<PersonDTO> people) {
        printLine();
        printHeader();
        printLine();
        people.forEach(personDTO -> {
            System.out.printf(getColumnsString(),
                personDTO.getId(),
                personDTO.getName(),
                personDTO.getStatus(),
                personDTO.getUsername(),
                personDTO.getPhone(),
                personDTO.getLastActiveAt());
            printLine();
        });

        System.out.printf("Page: %s of %s%nTotal elements: %s", people.getNumber() + 1, people.getTotalPages(), people.getTotalElements());
    }

    @NotNull
    private String getColumnsString() {
        return "|%4s|%20s|%10s|%20s|%15s|%20s|%n";
    }

    private void printHeader() {
        System.out.printf(getColumnsString(),
            Person_.ID, Person_.NAME, Person_.STATUS, Person_.USERNAME, Person_.PHONE, Person_.LAST_ACTIVE_AT);
    }

    private void printLine() {
        System.out.println("|" + Strings.repeat("-", 94) + "|");
    }
}
