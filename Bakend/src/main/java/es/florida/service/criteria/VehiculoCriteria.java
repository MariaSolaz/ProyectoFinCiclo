package es.florida.service.criteria;

import es.florida.domain.enumeration.EstadoVehiculo;
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

    /**
     * Class for filtering EstadoVehiculo
     */
    public static class EstadoVehiculoFilter extends Filter<EstadoVehiculo> {

        public EstadoVehiculoFilter() {}

        public EstadoVehiculoFilter(EstadoVehiculoFilter filter) {
            super(filter);
        }

        @Override
        public EstadoVehiculoFilter copy() {
            return new EstadoVehiculoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter matricula;

    private StringFilter marca;

    private StringFilter modelo;

    private LocalDateFilter anyo;

    private EstadoVehiculoFilter estado;

    private LongFilter registroId;

    private LongFilter duenyoId;

    private LongFilter mecanicoId;

    private LongFilter matriculaId;

    public VehiculoCriteria() {}

    public VehiculoCriteria(VehiculoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.matricula = other.matricula == null ? null : other.matricula.copy();
        this.marca = other.marca == null ? null : other.marca.copy();
        this.modelo = other.modelo == null ? null : other.modelo.copy();
        this.anyo = other.anyo == null ? null : other.anyo.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.registroId = other.registroId == null ? null : other.registroId.copy();
        this.duenyoId = other.duenyoId == null ? null : other.duenyoId.copy();
        this.mecanicoId = other.mecanicoId == null ? null : other.mecanicoId.copy();
        this.matriculaId = other.matriculaId == null ? null : other.matriculaId.copy();
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

    public EstadoVehiculoFilter getEstado() {
        return estado;
    }

    public EstadoVehiculoFilter estado() {
        if (estado == null) {
            estado = new EstadoVehiculoFilter();
        }
        return estado;
    }

    public void setEstado(EstadoVehiculoFilter estado) {
        this.estado = estado;
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
            Objects.equals(estado, that.estado) &&
            Objects.equals(registroId, that.registroId) &&
            Objects.equals(duenyoId, that.duenyoId) &&
            Objects.equals(mecanicoId, that.mecanicoId) &&
            Objects.equals(matriculaId, that.matriculaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, matricula, marca, modelo, anyo, estado, registroId, duenyoId, mecanicoId, matriculaId);
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
            (estado != null ? "estado=" + estado + ", " : "") +
            (registroId != null ? "registroId=" + registroId + ", " : "") +
            (duenyoId != null ? "duenyoId=" + duenyoId + ", " : "") +
            (mecanicoId != null ? "mecanicoId=" + mecanicoId + ", " : "") +
            (matriculaId != null ? "matriculaId=" + matriculaId + ", " : "") +
            "}";
    }
}
