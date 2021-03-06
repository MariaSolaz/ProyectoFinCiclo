package es.florida.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link es.florida.domain.Cliente} entity. This class is used
 * in {@link es.florida.web.rest.ClienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClienteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter apellido;

    private StringFilter dNI;

    private StringFilter telefono;

    private StringFilter correo;

    private LongFilter userId;

    private LongFilter duenyoId;

    public ClienteCriteria() {}

    public ClienteCriteria(ClienteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.apellido = other.apellido == null ? null : other.apellido.copy();
        this.dNI = other.dNI == null ? null : other.dNI.copy();
        this.telefono = other.telefono == null ? null : other.telefono.copy();
        this.correo = other.correo == null ? null : other.correo.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.duenyoId = other.duenyoId == null ? null : other.duenyoId.copy();
    }

    @Override
    public ClienteCriteria copy() {
        return new ClienteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNombre() {
        return nombre;
    }

    public StringFilter nombre() {
        if (nombre == null) {
            nombre = new StringFilter();
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getApellido() {
        return apellido;
    }

    public StringFilter apellido() {
        if (apellido == null) {
            apellido = new StringFilter();
        }
        return apellido;
    }

    public void setApellido(StringFilter apellido) {
        this.apellido = apellido;
    }

    public StringFilter getdNI() {
        return dNI;
    }

    public StringFilter dNI() {
        if (dNI == null) {
            dNI = new StringFilter();
        }
        return dNI;
    }

    public void setdNI(StringFilter dNI) {
        this.dNI = dNI;
    }

    public StringFilter getTelefono() {
        return telefono;
    }

    public StringFilter telefono() {
        if (telefono == null) {
            telefono = new StringFilter();
        }
        return telefono;
    }

    public void setTelefono(StringFilter telefono) {
        this.telefono = telefono;
    }

    public StringFilter getCorreo() {
        return correo;
    }

    public StringFilter correo() {
        if (correo == null) {
            correo = new StringFilter();
        }
        return correo;
    }

    public void setCorreo(StringFilter correo) {
        this.correo = correo;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getDuenyoId() {
        return duenyoId;
    }

    public LongFilter duenyoId() {
        if (duenyoId == null) {
            duenyoId = new LongFilter();
        }
        return duenyoId;
    }

    public void setDuenyoId(LongFilter duenyoId) {
        this.duenyoId = duenyoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClienteCriteria that = (ClienteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(apellido, that.apellido) &&
            Objects.equals(dNI, that.dNI) &&
            Objects.equals(telefono, that.telefono) &&
            Objects.equals(correo, that.correo) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(duenyoId, that.duenyoId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, apellido, dNI, telefono, correo, userId, duenyoId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (apellido != null ? "apellido=" + apellido + ", " : "") +
            (dNI != null ? "dNI=" + dNI + ", " : "") +
            (telefono != null ? "telefono=" + telefono + ", " : "") +
            (correo != null ? "correo=" + correo + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (duenyoId != null ? "duenyoId=" + duenyoId + ", " : "") +
            "}";
    }
}
