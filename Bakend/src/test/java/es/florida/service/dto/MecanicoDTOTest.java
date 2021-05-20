package es.florida.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import es.florida.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MecanicoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MecanicoDTO.class);
        MecanicoDTO mecanicoDTO1 = new MecanicoDTO();
        mecanicoDTO1.setId(1L);
        MecanicoDTO mecanicoDTO2 = new MecanicoDTO();
        assertThat(mecanicoDTO1).isNotEqualTo(mecanicoDTO2);
        mecanicoDTO2.setId(mecanicoDTO1.getId());
        assertThat(mecanicoDTO1).isEqualTo(mecanicoDTO2);
        mecanicoDTO2.setId(2L);
        assertThat(mecanicoDTO1).isNotEqualTo(mecanicoDTO2);
        mecanicoDTO1.setId(null);
        assertThat(mecanicoDTO1).isNotEqualTo(mecanicoDTO2);
    }
}
