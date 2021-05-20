package es.florida.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehiculoMapperTest {

    private VehiculoMapper vehiculoMapper;

    @BeforeEach
    public void setUp() {
        vehiculoMapper = new VehiculoMapperImpl();
    }
}
