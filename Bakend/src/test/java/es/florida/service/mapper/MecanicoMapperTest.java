package es.florida.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MecanicoMapperTest {

    private MecanicoMapper mecanicoMapper;

    @BeforeEach
    public void setUp() {
        mecanicoMapper = new MecanicoMapperImpl();
    }
}
