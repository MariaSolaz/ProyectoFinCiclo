import { IVehiculo } from 'app/entities/vehiculo/vehiculo.model';

export interface ICliente {
  id?: number;
  nombre?: string;
  apellido?: string;
  dNI?: string;
  telefono?: string;
  correo?: string;
  vehiculo?: IVehiculo | null;
}

export class Cliente implements ICliente {
  constructor(
    public id?: number,
    public nombre?: string,
    public apellido?: string,
    public dNI?: string,
    public telefono?: string,
    public correo?: string,
    public vehiculo?: IVehiculo | null
  ) {}
}

export function getClienteIdentifier(cliente: ICliente): number | undefined {
  return cliente.id;
}
