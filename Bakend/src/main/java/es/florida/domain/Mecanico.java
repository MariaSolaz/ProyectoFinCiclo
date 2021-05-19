package es.florida.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Mecanico.
 */
@Entity
@Table(name = "mecanico")
public class Mecanico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "apellido", nullable = false)
    private String apellido;

    @NotNull
    @Column(name = "d_ni", nullable = false)
    private String dNI;

    @NotNull
    @Column(name = "telefono", nullable = false)
    private String telefono;

    @NotNull
    @Column(name = "correo", nullable = false)
    private String correo;

    @OneToMany(mappedBy = "mecanico")
    @JsonIgnoreProperties(value = { "registros", "matriculas", "cliente", "mecanico" }, allowSetters = true)
    private Set<Vehiculo> mecanicos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mecanico id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Mecanico nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Mecanico apellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getdNI() {
        return this.dNI;
    }

    public Mecanico dNI(String dNI) {
        this.dNI = dNI;
        return this;
    }

    public void setdNI(String dNI) {
        this.dNI = dNI;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Mecanico telefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return this.correo;
    }

    public Mecanico correo(String correo) {
        this.correo = correo;
        return this;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Set<Vehiculo> getMecanicos() {
        return this.mecanicos;
    }

    public Mecanico mecanicos(Set<Vehiculo> vehiculos) {
        this.setMecanicos(vehiculos);
        return this;
    }

    public Mecanico addMecanico(Vehiculo vehiculo) {
        this.mecanicos.add(vehiculo);
        vehiculo.setMecanico(this);
        return this;
    }

    public Mecanico removeMecanico(Vehiculo vehiculo) {
        this.mecanicos.remove(vehiculo);
        vehiculo.setMecanico(null);
        return this;
    }

    public void setMecanicos(Set<Vehiculo> vehiculos) {
        if (this.mecanicos != null) {
            this.mecanicos.forEach(i -> i.setMecanico(null));
        }
        if (vehiculos != null) {
            vehiculos.forEach(i -> i.setMecanico(this));
        }
        this.mecanicos = vehiculos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mecanico)) {
            return false;
        }
        return id != null && id.equals(((Mecanico) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mecanico{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", dNI='" + getdNI() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", correo='" + getCorreo() + "'" +
            "}";
    }
}
