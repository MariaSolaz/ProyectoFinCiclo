import * as dayjs from 'dayjs';

export interface ICliente {
    id?:number;
    nombre?: string;
    apellido?: string;
    dNI?: string;
    telefono?: string;
    correo?: string;
    vehiculo?: IVehiculo | null;
}
export class Cliente implements ICliente{
    constructor(
        public id?:number,
        public nombre?: string,
        public apellido?: string,
        public dNI?: string,
        public telefono?: string,
        public correo?: string,
        public vehiculo?: IVehiculo | null,
    ){}
}
export function getClienteIdentifier(cliente: ICliente): number | undefined {
    return cliente.id;
}

export interface IVehiculo {
    id?: number;
    matricula?: string;
    marca?: string;
    modelo?: string;
    anyo?: dayjs.Dayjs;
    estado?: EstadoVehiculo | null;
    duenyos?: ICliente[] | null;
    mecanicos?: IMecanico[] | null;
    factura?: IFactura[] | null;
}

export class Vehiculo implements IVehiculo{
    constructor(
        public id?:number,
        public matricula?: string,
        public marca?: string,
        public modelo?: string,
        public anyo?: dayjs.Dayjs,
        public estado?: EstadoVehiculo | null,
        public duenyos?: ICliente[] | null,
        public mecanicos?: IMecanico[] | null,
        public factura?: IFactura[] | null,
    ){}
}
export function getVehiculoIdentifier(vehiculo: IVehiculo): number | undefined {
    return vehiculo.id;
}


export interface IFactura {
    id?: number;
    fecha?: dayjs.Dayjs;
    diagnostico?: string;
    precio?: number;
    estado?: EstadoFactura | null;
    vehiculo?: IVehiculo | null;
}
export class Factura implements IFactura{
    constructor(
        public id?: number,
        public fecha?: dayjs.Dayjs,
        public diagnostico?: string,
        public precio?: number,
        public estado?: EstadoFactura | null,
        public vehiculo?: IVehiculo | null
    ){}
}

export function getFacturaIdentifier(factura: IFactura): number | undefined {
    return factura.id;
}

export interface IMecanico {
    id?: number;
    nombre?: string;
    apellido?: string;
    dNI?: string;
    telefono?: string;
    correo?: string;
    vehiculo?: IVehiculo | null;
}

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
    ){}
}
  
export function getRegistroIdentifier(registro: IRegistro): number | undefined {
    return registro.id;
}
  

export enum EstadoFactura {
    Aceptada = 'Aceptada',
  
    Declinada = 'Declinada',
  
    Pendiente = 'Pendiente',
}

export enum EstadoVehiculo {
    NoRevisado = 'NoRevisado',
  
    Revisado = 'Revisado',
  
    Reparando = 'Reparando',
  
    Finalizado = 'Finalizado',
}