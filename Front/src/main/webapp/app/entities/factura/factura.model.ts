import * as dayjs from 'dayjs';
import { IVehiculo } from 'app/entities/vehiculo/vehiculo.model';
import { EstadoFactura } from 'app/entities/enumerations/estado-factura.model';

export interface IFactura {
  id?: number;
  fecha?: dayjs.Dayjs;
  diagnostico?: string;
  precio?: number;
  estado?: EstadoFactura | null;
  vehiculo?: IVehiculo | null;
}

export class Factura implements IFactura {
  constructor(
    public id?: number,
    public fecha?: dayjs.Dayjs,
    public diagnostico?: string,
    public precio?: number,
    public estado?: EstadoFactura | null,
    public vehiculo?: IVehiculo | null
  ) {}
}

export function getFacturaIdentifier(factura: IFactura): number | undefined {
  return factura.id;
}
