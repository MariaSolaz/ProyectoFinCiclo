package es.florida.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @OneToMany(mappedBy = "vehiculo")
    @JsonIgnoreProperties(value = { "vehiculo" }, allowSetters = true)
    private Set<Registro> registros = new HashSet<>();

    @OneToMany(mappedBy = "vehiculo")
    @JsonIgnoreProperties(value = { "vehiculo" }, allowSetters = true)
    private Set<Factura> matriculas = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "duenyos" }, allowSetters = true)
    private Cliente cliente;

    @ManyToOne
    @JsonIgnoreProperties(value = { "mecanicos" }, allowSetters = true)
    private Mecanico mecanico;

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

    public Set<Registro> getRegistros() {
        return this.registros;
    }

    public Vehiculo registros(Set<Registro> registros) {
        this.setRegistros(registros);
        return this;
    }

    public Vehiculo addRegistro(Registro registro) {
        this.registros.add(registro);
        registro.setVehiculo(this);
        return this;
    }

    public Vehiculo removeRegistro(Registro registro) {
        this.registros.remove(registro);
        registro.setVehiculo(null);
        return this;
    }

    public void setRegistros(Set<Registro> registros) {
        if (this.registros != null) {
            this.registros.forEach(i -> i.setVehiculo(null));
        }
        if (registros != null) {
            registros.forEach(i -> i.setVehiculo(this));
        }
        this.registros = registros;
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

    public Cliente getCliente() {
        return this.cliente;
    }

    public Vehiculo cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Mecanico getMecanico() {
        return this.mecanico;
    }

    public Vehiculo mecanico(Mecanico mecanico) {
        this.setMecanico(mecanico);
        return this;
    }

    public void setMecanico(Mecanico mecanico) {
        this.mecanico = mecanico;
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
            "}";
    }
}
