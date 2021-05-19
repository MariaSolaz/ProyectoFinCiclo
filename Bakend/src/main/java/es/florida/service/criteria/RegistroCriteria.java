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
 * Criteria class for the {@link es.florida.domain.Registro} entity. This class is used
 * in {@link es.florida.web.rest.RegistroResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /registros?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RegistroCriteria implements Serializable, Criteria {

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

    private LocalDateFilter fecha;

    private EstadoVehiculoFilter estadoActual;

    private LongFilter vehiculoId;

    public RegistroCriteria() {}

    public RegistroCriteria(RegistroCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.estadoActual = other.estadoActual == null ? null : other.estadoActual.copy();
        this.vehiculoId = other.vehiculoId == null ? null : other.vehiculoId.copy();
    }

    @Override
    public RegistroCriteria copy() {
        return new RegistroCriteria(this);
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

    public LocalDateFilter getFecha() {
        return fecha;
    }

    public LocalDateFilter fecha() {
        if (fecha == null) {
            fecha = new LocalDateFilter();
        }
        return fecha;
    }

    public void setFecha(LocalDateFilter fecha) {
        this.fecha = fecha;
    }

    public EstadoVehiculoFilter getEstadoActual() {
        return estadoActual;
    }

    public EstadoVehiculoFilter estadoActual() {
        if (estadoActual == null) {
            estadoActual = new EstadoVehiculoFilter();
        }
        return estadoActual;
    }

    public void setEstadoActual(EstadoVehiculoFilter estadoActual) {
        this.estadoActual = estadoActual;
    }

    public LongFilter getVehiculoId() {
        return vehiculoId;
    }

    public LongFilter vehiculoId() {
        if (vehiculoId == null) {
            vehiculoId = new LongFilter();
        }
        return vehiculoId;
    }

    public void setVehiculoId(LongFilter vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RegistroCriteria that = (RegistroCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(estadoActual, that.estadoActual) &&
            Objects.equals(vehiculoId, that.vehiculoId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fecha, estadoActual, vehiculoId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegistroCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fecha != null ? "fecha=" + fecha + ", " : "") +
            (estadoActual != null ? "estadoActual=" + estadoActual + ", " : "") +
            (vehiculoId != null ? "vehiculoId=" + vehiculoId + ", " : "") +
            "}";
    }
}
