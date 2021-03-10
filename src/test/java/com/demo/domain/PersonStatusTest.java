package com.demo.domain;

import com.demo.domain.enumeration.PStatus;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.demo.web.rest.TestUtil;

public class PersonStatusTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonStatus.class);
        PersonStatus personStatus1 = new PersonStatus();
        personStatus1.setCode(PStatus.ONLINE);
        PersonStatus personStatus2 = new PersonStatus();
        personStatus2.setCode(personStatus1.getCode());
        assertThat(personStatus1).isEqualTo(personStatus2);
        personStatus2.setCode(PStatus.BUSY);
        assertThat(personStatus1).isNotEqualTo(personStatus2);
        personStatus1.setCode(null);
        assertThat(personStatus1).isNotEqualTo(personStatus2);
    }
}
