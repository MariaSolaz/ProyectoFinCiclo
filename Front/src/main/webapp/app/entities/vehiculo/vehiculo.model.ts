import * as dayjs from 'dayjs';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { IMecanico } from 'app/entities/mecanico/mecanico.model';
import { IFactura } from 'app/entities/factura/factura.model';
import { EstadoVehiculo } from 'app/entities/enumerations/estado-vehiculo.model';

export interface IVehiculo {
  id?: number;
  matricula?: string;
  marca?: string;
  modelo?: string;
  anyo?: dayjs.Dayjs;
  estado?: EstadoVehiculo | null;
  duenyos?: ICliente[] | null;
  mecanicos?: IMecanico[] | null;
  matriculas?: IFactura[] | null;
}

export class Vehiculo implements IVehiculo {
  constructor(
    public id?: number,
    public matricula?: string,
    public marca?: string,
    public modelo?: string,
    public anyo?: dayjs.Dayjs,
    public estado?: EstadoVehiculo | null,
    public duenyos?: ICliente[] | null,
    public mecanicos?: IMecanico[] | null,
    public matriculas?: IFactura[] | null
  ) {}
}

export function getVehiculoIdentifier(vehiculo: IVehiculo): number | undefined {
  return vehiculo.id;
}
