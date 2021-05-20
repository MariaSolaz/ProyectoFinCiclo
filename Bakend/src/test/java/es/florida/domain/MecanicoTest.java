package es.florida.domain;

import static org.assertj.core.api.Assertions.assertThat;

import es.florida.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MecanicoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mecanico.class);
        Mecanico mecanico1 = new Mecanico();
        mecanico1.setId(1L);
        Mecanico mecanico2 = new Mecanico();
        mecanico2.setId(mecanico1.getId());
        assertThat(mecanico1).isEqualTo(mecanico2);
        mecanico2.setId(2L);
        assertThat(mecanico1).isNotEqualTo(mecanico2);
        mecanico1.setId(null);
        assertThat(mecanico1).isNotEqualTo(mecanico2);
    }
}
