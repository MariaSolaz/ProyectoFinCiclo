package es.florida.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import es.florida.domain.enumeration.EstadoVehiculo;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Registro.
 */
@Entity
@Table(name = "registro")
public class Registro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_actual")
    private EstadoVehiculo estadoActual;

    @ManyToOne
    @JsonIgnoreProperties(value = { "registros", "matriculas", "cliente", "mecanico" }, allowSetters = true)
    private Vehiculo vehiculo;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Registro id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Registro fecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public EstadoVehiculo getEstadoActual() {
        return this.estadoActual;
    }

    public Registro estadoActual(EstadoVehiculo estadoActual) {
        this.estadoActual = estadoActual;
        return this;
    }

    public void setEstadoActual(EstadoVehiculo estadoActual) {
        this.estadoActual = estadoActual;
    }

    public Vehiculo getVehiculo() {
        return this.vehiculo;
    }

    public Registro vehiculo(Vehiculo vehiculo) {
        this.setVehiculo(vehiculo);
        return this;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Registro)) {
            return false;
        }
        return id != null && id.equals(((Registro) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Registro{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", estadoActual='" + getEstadoActual() + "'" +
            "}";
    }
}
