package com.demo.service;

import com.demo.SpecificationsApp;
import com.demo.config.TestSecurityConfiguration;
import com.demo.domain.Person_;
import com.demo.service.dto.PersonCriteria;
import com.google.common.base.Strings;
import io.github.jhipster.service.filter.LongFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

@SpringBootTest(classes = {SpecificationsApp.class, TestSecurityConfiguration.class})
public class PersonQueryServiceTest {

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

        // When
        var people = personQueryService.findByCriteria(criteria);

        // Then
        printResult(people);
    }

    private void printResult(java.util.List<com.demo.service.dto.PersonDTO> people) {
        printLine();
        printHeader();
        printLine();
        people.forEach(personDTO -> {
            System.out.printf("|%4s|%20s|%10s|%20s|%15s|%n",
                personDTO.getId(), personDTO.getName(), personDTO.getStatus(), personDTO.getUsername(), personDTO.getPhone());
            printLine();
        });
    }

    private void printHeader() {
        System.out.printf("|%4s|%20s|%10s|%20s|%15s|%n",
            Person_.ID, Person_.NAME, Person_.STATUS, Person_.USERNAME, Person_.PHONE);
    }

    private void printLine() {
        System.out.println("|" + Strings.repeat("-", 73) + "|");
    }
}
