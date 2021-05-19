package es.florida.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import es.florida.domain.enumeration.EstadoFactura;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Type;

/**
 * A Factura.
 */
@Entity
@Table(name = "factura")
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "diagnostico", nullable = false)
    private String diagnostico;

    @NotNull
    @Column(name = "precio", nullable = false)
    private Double precio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoFactura estado;

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

    public Factura id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Factura fecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDiagnostico() {
        return this.diagnostico;
    }

    public Factura diagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
        return this;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Double getPrecio() {
        return this.precio;
    }

    public Factura precio(Double precio) {
        this.precio = precio;
        return this;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public EstadoFactura getEstado() {
        return this.estado;
    }

    public Factura estado(EstadoFactura estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(EstadoFactura estado) {
        this.estado = estado;
    }

    public Vehiculo getVehiculo() {
        return this.vehiculo;
    }

    public Factura vehiculo(Vehiculo vehiculo) {
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
        if (!(o instanceof Factura)) {
            return false;
        }
        return id != null && id.equals(((Factura) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Factura{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", diagnostico='" + getDiagnostico() + "'" +
            ", precio=" + getPrecio() +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
