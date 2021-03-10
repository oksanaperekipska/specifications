package com.demo.service.mapper;

import com.demo.domain.enumeration.PStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PersonStatusMapperTest {

    private PersonStatusMapper personStatusMapper;

    @BeforeEach
    public void setUp() {
        personStatusMapper = new PersonStatusMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        PStatus code = PStatus.ONLINE;
        assertThat(personStatusMapper.fromId(code).getCode()).isEqualTo(code);
        assertThat(personStatusMapper.fromId(null)).isNull();
    }
}
