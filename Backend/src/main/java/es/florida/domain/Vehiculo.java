package es.florida.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import es.florida.domain.enumeration.EstadoVehiculo;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Vehiculo.
 */
@Entity
@Table(name = "vehiculo")
public class Vehiculo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "matricula", nullable = false)
    private String matricula;

    @NotNull
    @Column(name = "marca", nullable = false)
    private String marca;

    @NotNull
    @Column(name = "modelo", nullable = false)
    private String modelo;

    @NotNull
    @Column(name = "anyo", nullable = false)
    private LocalDate anyo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoVehiculo estado;

    @OneToMany(mappedBy = "vehiculo")
    @JsonIgnoreProperties(value = { "vehiculo" }, allowSetters = true)
    private Set<Cliente> duenyos = new HashSet<>();

    @OneToMany(mappedBy = "vehiculo")
    @JsonIgnoreProperties(value = { "vehiculo" }, allowSetters = true)
    private Set<Mecanico> mecanicos = new HashSet<>();

    @OneToMany(mappedBy = "vehiculo")
    @JsonIgnoreProperties(value = { "vehiculo" }, allowSetters = true)
    private Set<Factura> matriculas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehiculo id(Long id) {
        this.id = id;
        return this;
    }

    public String getMatricula() {
        return this.matricula;
    }

    public Vehiculo matricula(String matricula) {
        this.matricula = matricula;
        return this;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return this.marca;
    }

    public Vehiculo marca(String marca) {
        this.marca = marca;
        return this;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return this.modelo;
    }

    public Vehiculo modelo(String modelo) {
        this.modelo = modelo;
        return this;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public LocalDate getAnyo() {
        return this.anyo;
    }

    public Vehiculo anyo(LocalDate anyo) {
        this.anyo = anyo;
        return this;
    }

    public void setAnyo(LocalDate anyo) {
        this.anyo = anyo;
    }

    public EstadoVehiculo getEstado() {
        return this.estado;
    }

    public Vehiculo estado(EstadoVehiculo estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(EstadoVehiculo estado) {
        this.estado = estado;
    }

    public Set<Cliente> getDuenyos() {
        return this.duenyos;
    }

    public Vehiculo duenyos(Set<Cliente> clientes) {
        this.setDuenyos(clientes);
        return this;
    }

    public Vehiculo addDuenyo(Cliente cliente) {
        this.duenyos.add(cliente);
        cliente.setVehiculo(this);
        return this;
    }

    public Vehiculo removeDuenyo(Cliente cliente) {
        this.duenyos.remove(cliente);
        cliente.setVehiculo(null);
        return this;
    }

    public void setDuenyos(Set<Cliente> clientes) {
        if (this.duenyos != null) {
            this.duenyos.forEach(i -> i.setVehiculo(null));
        }
        if (clientes != null) {
            clientes.forEach(i -> i.setVehiculo(this));
        }
        this.duenyos = clientes;
    }

    public Set<Mecanico> getMecanicos() {
        return this.mecanicos;
    }

    public Vehiculo mecanicos(Set<Mecanico> mecanicos) {
        this.setMecanicos(mecanicos);
        return this;
    }

    public Vehiculo addMecanico(Mecanico mecanico) {
        this.mecanicos.add(mecanico);
        mecanico.setVehiculo(this);
        return this;
    }

    public Vehiculo removeMecanico(Mecanico mecanico) {
        this.mecanicos.remove(mecanico);
        mecanico.setVehiculo(null);
        return this;
    }

    public void setMecanicos(Set<Mecanico> mecanicos) {
        if (this.mecanicos != null) {
            this.mecanicos.forEach(i -> i.setVehiculo(null));
        }
        if (mecanicos != null) {
            mecanicos.forEach(i -> i.setVehiculo(this));
        }
        this.mecanicos = mecanicos;
    }

    public Set<Factura> getMatriculas() {
        return this.matriculas;
    }

    public Vehiculo matriculas(Set<Factura> facturas) {
        this.setMatriculas(facturas);
        return this;
    }

    public Vehiculo addMatricula(Factura factura) {
        this.matriculas.add(factura);
        factura.setVehiculo(this);
        return this;
    }

    public Vehiculo removeMatricula(Factura factura) {
        this.matriculas.remove(factura);
        factura.setVehiculo(null);
        return this;
    }

    public void setMatriculas(Set<Factura> facturas) {
        if (this.matriculas != null) {
            this.matriculas.forEach(i -> i.setVehiculo(null));
        }
        if (facturas != null) {
            facturas.forEach(i -> i.setVehiculo(this));
        }
        this.matriculas = facturas;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vehiculo)) {
            return false;
        }
        return id != null && id.equals(((Vehiculo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vehiculo{" +
            "id=" + getId() +
            ", matricula='" + getMatricula() + "'" +
            ", marca='" + getMarca() + "'" +
            ", modelo='" + getModelo() + "'" +
            ", anyo='" + getAnyo() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
