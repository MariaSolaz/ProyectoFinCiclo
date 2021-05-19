package es.florida.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link es.florida.domain.Vehiculo} entity. This class is used
 * in {@link es.florida.web.rest.VehiculoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vehiculos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VehiculoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter matricula;

    private StringFilter marca;

    private StringFilter modelo;

    private LocalDateFilter anyo;

    private LongFilter registroId;

    private LongFilter matriculaId;

    private LongFilter clienteId;

    private LongFilter mecanicoId;

    public VehiculoCriteria() {}

    public VehiculoCriteria(VehiculoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.matricula = other.matricula == null ? null : other.matricula.copy();
        this.marca = other.marca == null ? null : other.marca.copy();
        this.modelo = other.modelo == null ? null : other.modelo.copy();
        this.anyo = other.anyo == null ? null : other.anyo.copy();
        this.registroId = other.registroId == null ? null : other.registroId.copy();
        this.matriculaId = other.matriculaId == null ? null : other.matriculaId.copy();
        this.clienteId = other.clienteId == null ? null : other.clienteId.copy();
        this.mecanicoId = other.mecanicoId == null ? null : other.mecanicoId.copy();
    }

    @Override
    public VehiculoCriteria copy() {
        return new VehiculoCriteria(this);
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

    public StringFilter getMatricula() {
        return matricula;
    }

    public StringFilter matricula() {
        if (matricula == null) {
            matricula = new StringFilter();
        }
        return matricula;
    }

    public void setMatricula(StringFilter matricula) {
        this.matricula = matricula;
    }

    public StringFilter getMarca() {
        return marca;
    }

    public StringFilter marca() {
        if (marca == null) {
            marca = new StringFilter();
        }
        return marca;
    }

    public void setMarca(StringFilter marca) {
        this.marca = marca;
    }

    public StringFilter getModelo() {
        return modelo;
    }

    public StringFilter modelo() {
        if (modelo == null) {
            modelo = new StringFilter();
        }
        return modelo;
    }

    public void setModelo(StringFilter modelo) {
        this.modelo = modelo;
    }

    public LocalDateFilter getAnyo() {
        return anyo;
    }

    public LocalDateFilter anyo() {
        if (anyo == null) {
            anyo = new LocalDateFilter();
        }
        return anyo;
    }

    public void setAnyo(LocalDateFilter anyo) {
        this.anyo = anyo;
    }

    public LongFilter getRegistroId() {
        return registroId;
    }

    public LongFilter registroId() {
        if (registroId == null) {
            registroId = new LongFilter();
        }
        return registroId;
    }

    public void setRegistroId(LongFilter registroId) {
        this.registroId = registroId;
    }

    public LongFilter getMatriculaId() {
        return matriculaId;
    }

    public LongFilter matriculaId() {
        if (matriculaId == null) {
            matriculaId = new LongFilter();
        }
        return matriculaId;
    }

    public void setMatriculaId(LongFilter matriculaId) {
        this.matriculaId = matriculaId;
    }

    public LongFilter getClienteId() {
        return clienteId;
    }

    public LongFilter clienteId() {
        if (clienteId == null) {
            clienteId = new LongFilter();
        }
        return clienteId;
    }

    public void setClienteId(LongFilter clienteId) {
        this.clienteId = clienteId;
    }

    public LongFilter getMecanicoId() {
        return mecanicoId;
    }

    public LongFilter mecanicoId() {
        if (mecanicoId == null) {
            mecanicoId = new LongFilter();
        }
        return mecanicoId;
    }

    public void setMecanicoId(LongFilter mecanicoId) {
        this.mecanicoId = mecanicoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VehiculoCriteria that = (VehiculoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(matricula, that.matricula) &&
            Objects.equals(marca, that.marca) &&
            Objects.equals(modelo, that.modelo) &&
            Objects.equals(anyo, that.anyo) &&
            Objects.equals(registroId, that.registroId) &&
            Objects.equals(matriculaId, that.matriculaId) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(mecanicoId, that.mecanicoId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, matricula, marca, modelo, anyo, registroId, matriculaId, clienteId, mecanicoId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehiculoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (matricula != null ? "matricula=" + matricula + ", " : "") +
            (marca != null ? "marca=" + marca + ", " : "") +
            (modelo != null ? "modelo=" + modelo + ", " : "") +
            (anyo != null ? "anyo=" + anyo + ", " : "") +
            (registroId != null ? "registroId=" + registroId + ", " : "") +
            (matriculaId != null ? "matriculaId=" + matriculaId + ", " : "") +
            (clienteId != null ? "clienteId=" + clienteId + ", " : "") +
            (mecanicoId != null ? "mecanicoId=" + mecanicoId + ", " : "") +
            "}";
    }
}
