import * as dayjs from 'dayjs';
import { IRegistro } from 'app/entities/registro/registro.model';
import { IFactura } from 'app/entities/factura/factura.model';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { IMecanico } from 'app/entities/mecanico/mecanico.model';

export interface IVehiculo {
  id?: number;
  matricula?: string;
  marca?: string;
  modelo?: string;
  anyo?: dayjs.Dayjs;
  registros?: IRegistro[] | null;
  matriculas?: IFactura[] | null;
  cliente?: ICliente | null;
  mecanico?: IMecanico | null;
}

export class Vehiculo implements IVehiculo {
  constructor(
    public id?: number,
    public matricula?: string,
    public marca?: string,
    public modelo?: string,
    public anyo?: dayjs.Dayjs,
    public registros?: IRegistro[] | null,
    public matriculas?: IFactura[] | null,
    public cliente?: ICliente | null,
    public mecanico?: IMecanico | null
  ) {}
}

export function getVehiculoIdentifier(vehiculo: IVehiculo): number | undefined {
  return vehiculo.id;
}
