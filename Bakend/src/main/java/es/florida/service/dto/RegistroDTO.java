package es.florida.service.dto;

import es.florida.domain.enumeration.EstadoVehiculo;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link es.florida.domain.Registro} entity.
 */
public class RegistroDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate fecha;

    private EstadoVehiculo estadoActual;

    private VehiculoDTO vehiculo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public EstadoVehiculo getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(EstadoVehiculo estadoActual) {
        this.estadoActual = estadoActual;
    }

    public VehiculoDTO getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(VehiculoDTO vehiculo) {
        this.vehiculo = vehiculo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegistroDTO)) {
            return false;
        }

        RegistroDTO registroDTO = (RegistroDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, registroDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegistroDTO{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", estadoActual='" + getEstadoActual() + "'" +
            ", vehiculo=" + getVehiculo() +
            "}";
    }
}
