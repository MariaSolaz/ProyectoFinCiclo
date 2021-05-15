import * as dayjs from 'dayjs';
import { IVehiculo } from 'app/entities/vehiculo/vehiculo.model';
import { EstadoVehiculo } from 'app/entities/enumerations/estado-vehiculo.model';

export interface IRegistro {
  id?: number;
  fecha?: dayjs.Dayjs;
  estadoActual?: EstadoVehiculo | null;
  vehiculo?: IVehiculo | null;
}

export class Registro implements IRegistro {
  constructor(
    public id?: number,
    public fecha?: dayjs.Dayjs,
    public estadoActual?: EstadoVehiculo | null,
    public vehiculo?: IVehiculo | null
  ) {}
}

export function getRegistroIdentifier(registro: IRegistro): number | undefined {
  return registro.id;
}
