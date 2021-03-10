package com.demo.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.demo.web.rest.TestUtil;

public class PersonStatusDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonStatusDTO.class);
        PersonStatusDTO personStatusDTO1 = new PersonStatusDTO();
        personStatusDTO1.setId(1L);
        PersonStatusDTO personStatusDTO2 = new PersonStatusDTO();
        assertThat(personStatusDTO1).isNotEqualTo(personStatusDTO2);
        personStatusDTO2.setId(personStatusDTO1.getId());
        assertThat(personStatusDTO1).isEqualTo(personStatusDTO2);
        personStatusDTO2.setId(2L);
        assertThat(personStatusDTO1).isNotEqualTo(personStatusDTO2);
        personStatusDTO1.setId(null);
        assertThat(personStatusDTO1).isNotEqualTo(personStatusDTO2);
    }
}
