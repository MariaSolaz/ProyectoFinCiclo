package es.florida.service.dto;

import es.florida.domain.enumeration.EstadoVehiculo;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link es.florida.domain.Vehiculo} entity.
 */
public class VehiculoDTO implements Serializable {

    private Long id;

    @NotNull
    private String matricula;

    @NotNull
    private String marca;

    @NotNull
    private String modelo;

    @NotNull
    private LocalDate anyo;

    private EstadoVehiculo estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public LocalDate getAnyo() {
        return anyo;
    }

    public void setAnyo(LocalDate anyo) {
        this.anyo = anyo;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public void setEstado(EstadoVehiculo estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehiculoDTO)) {
            return false;
        }

        VehiculoDTO vehiculoDTO = (VehiculoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vehiculoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehiculoDTO{" +
            "id=" + getId() +
            ", matricula='" + getMatricula() + "'" +
            ", marca='" + getMarca() + "'" +
            ", modelo='" + getModelo() + "'" +
            ", anyo='" + getAnyo() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
