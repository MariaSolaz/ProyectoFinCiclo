package es.florida.service.criteria;

import es.florida.domain.enumeration.EstadoFactura;
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
 * Criteria class for the {@link es.florida.domain.Factura} entity. This class is used
 * in {@link es.florida.web.rest.FacturaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /facturas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FacturaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EstadoFactura
     */
    public static class EstadoFacturaFilter extends Filter<EstadoFactura> {

        public EstadoFacturaFilter() {}

        public EstadoFacturaFilter(EstadoFacturaFilter filter) {
            super(filter);
        }

        @Override
        public EstadoFacturaFilter copy() {
            return new EstadoFacturaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fecha;

    private DoubleFilter precio;

    private EstadoFacturaFilter estado;

    private LongFilter vehiculoId;

    public FacturaCriteria() {}

    public FacturaCriteria(FacturaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.precio = other.precio == null ? null : other.precio.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.vehiculoId = other.vehiculoId == null ? null : other.vehiculoId.copy();
    }

    @Override
    public FacturaCriteria copy() {
        return new FacturaCriteria(this);
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

    public DoubleFilter getPrecio() {
        return precio;
    }

    public DoubleFilter precio() {
        if (precio == null) {
            precio = new DoubleFilter();
        }
        return precio;
    }

    public void setPrecio(DoubleFilter precio) {
        this.precio = precio;
    }

    public EstadoFacturaFilter getEstado() {
        return estado;
    }

    public EstadoFacturaFilter estado() {
        if (estado == null) {
            estado = new EstadoFacturaFilter();
        }
        return estado;
    }

    public void setEstado(EstadoFacturaFilter estado) {
        this.estado = estado;
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
        final FacturaCriteria that = (FacturaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(precio, that.precio) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(vehiculoId, that.vehiculoId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fecha, precio, estado, vehiculoId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fecha != null ? "fecha=" + fecha + ", " : "") +
            (precio != null ? "precio=" + precio + ", " : "") +
            (estado != null ? "estado=" + estado + ", " : "") +
            (vehiculoId != null ? "vehiculoId=" + vehiculoId + ", " : "") +
            "}";
    }
}
