import { IVehiculo } from 'app/entities/vehiculo/vehiculo.model';

export interface IMecanico {
  id?: number;
  nombre?: string;
  apellido?: string;
  dNI?: string;
  telefono?: string;
  correo?: string;
  mecanicos?: IVehiculo[] | null;
}

export class Mecanico implements IMecanico {
  constructor(
    public id?: number,
    public nombre?: string,
    public apellido?: string,
    public dNI?: string,
    public telefono?: string,
    public correo?: string,
    public mecanicos?: IVehiculo[] | null
  ) {}
}

export function getMecanicoIdentifier(mecanico: IMecanico): number | undefined {
  return mecanico.id;
}
