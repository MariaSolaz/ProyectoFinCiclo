package es.florida.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
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

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnoreProperties(value = { "registros", "matriculas", "cliente", "mecanico" }, allowSetters = true)
    private Set<Vehiculo> duenyos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Cliente nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Cliente apellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getdNI() {
        return this.dNI;
    }

    public Cliente dNI(String dNI) {
        this.dNI = dNI;
        return this;
    }

    public void setdNI(String dNI) {
        this.dNI = dNI;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Cliente telefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return this.correo;
    }

    public Cliente correo(String correo) {
        this.correo = correo;
        return this;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public User getUser() {
        return this.user;
    }

    public Cliente user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Vehiculo> getDuenyos() {
        return this.duenyos;
    }

    public Cliente duenyos(Set<Vehiculo> vehiculos) {
        this.setDuenyos(vehiculos);
        return this;
    }

    public Cliente addDuenyo(Vehiculo vehiculo) {
        this.duenyos.add(vehiculo);
        vehiculo.setCliente(this);
        return this;
    }

    public Cliente removeDuenyo(Vehiculo vehiculo) {
        this.duenyos.remove(vehiculo);
        vehiculo.setCliente(null);
        return this;
    }

    public void setDuenyos(Set<Vehiculo> vehiculos) {
        if (this.duenyos != null) {
            this.duenyos.forEach(i -> i.setCliente(null));
        }
        if (vehiculos != null) {
            vehiculos.forEach(i -> i.setCliente(this));
        }
        this.duenyos = vehiculos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cliente)) {
            return false;
        }
        return id != null && id.equals(((Cliente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", dNI='" + getdNI() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", correo='" + getCorreo() + "'" +
            "}";
    }
}
