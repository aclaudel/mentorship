package aclaudel.codurance.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import aclaudel.codurance.web.rest.TestUtil;

public class CraftspersonTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Craftsperson.class);
        Craftsperson craftsperson1 = new Craftsperson();
        craftsperson1.setId(1L);
        Craftsperson craftsperson2 = new Craftsperson();
        craftsperson2.setId(craftsperson1.getId());
        assertThat(craftsperson1).isEqualTo(craftsperson2);
        craftsperson2.setId(2L);
        assertThat(craftsperson1).isNotEqualTo(craftsperson2);
        craftsperson1.setId(null);
        assertThat(craftsperson1).isNotEqualTo(craftsperson2);
    }
}
