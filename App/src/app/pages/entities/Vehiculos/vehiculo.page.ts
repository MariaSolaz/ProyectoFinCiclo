
import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FacturaService } from 'src/app/services/Factura/factura.service';


import { VehiculoService } from 'src/app/services/Vehiculo/vehiculo.service';
import {IFactura, IVehiculo, IRegistro, Registro} from '../../../interfaces'
import {RegistroService} from 'src/app/services/Registro/registro.service';

@Component({
    selector: 'app-vehiculo',
    templateUrl: 'vehiculo.html',
    styleUrls:['vehiculo.scss'],
})

export class VehiculoPage implements OnInit{
    
    vehiculos: IVehiculo[] = [];
    facturas: IFactura[] = [];
    registros: IRegistro [] = [];
    idVehiculo:number;
    idFactura:number;
    user:string;
    isLoading = false;
    totalItems: number;
    hideMeEstado:boolean;
    hideMeFactura:boolean;

    constructor(
        protected servicioVehiculo: VehiculoService,
        protected servicioFactura: FacturaService,
        protected servicioRegistro: RegistroService,
        protected activatedRoute: ActivatedRoute,
        protected router: Router,
        protected modalService: NgbModal,
    ){
        this.hideMeEstado = true;
        this.hideMeFactura = false;
    }

    cargarVehiculos(){
        this.isLoading = true;
        const filtros:Map<string,any> = new Map();
       filtros.set('id.equals', this.idVehiculo)
        this.servicioVehiculo.obtenerVehiculos({
            filter: filtros
        }).subscribe(
            (res: HttpResponse<IVehiculo[]>) => {

                this.pagianteVehiculo(res.body);
                console.log(res.body);
            },
            (error) =>{
                console.error(error);
            }
        );
    }

    cargarRegistros(){
        this.isLoading = true;
        const filtros:Map<string,any> = new Map();
       filtros.set('vehiculoId.equals', this.idVehiculo)
       this.servicioRegistro.obtenerRegistros({
           filter: filtros,
       }).subscribe(
           (res: HttpResponse<IRegistro[]>) => {
               this.pagianteRegistro(res.body);
               console.log(res.body);
           },
           (error) =>{
            console.log(error);
        }
       )
    }

    cargarFacturas(){
        this.isLoading = true;
        
        const filtros:Map<string,any> = new Map();
       filtros.set('vehiculoId.equals', this.idVehiculo)
        console.log(filtros)
        this.servicioFactura.obtenerFacturas({
            filter:filtros
        }).subscribe(
            (res: HttpResponse<IFactura[]>) => {
                this.pagianteFactura(res.body);
                console.log(res.body);
            },
            (error) =>{
                console.log(error);
            }
        )
    }

    loadAll(){
      this.cargarVehiculos();
      this.cargarFacturas();
      this.cargarRegistros();
    }

    hideEstado(){
        this.hideMeEstado = true;
        this.hideMeFactura = false;
    }
    hideFactura(){
        this.hideMeEstado = false;
        this.hideMeFactura = true;
    }

  
    ngOnInit(): void {
        this.idVehiculo = this.activatedRoute.snapshot.params.idVehiculo;
        this.user = this.activatedRoute.snapshot.params.nameuser;
        this.loadAll();
    }

    protected pagianteVehiculo(data: IVehiculo[] | null):void{
        if(data){
            for(const d of data){
               this.vehiculos.push(d);             
            }
        }
    }
    reset():void{
        this.facturas = [];
        this.vehiculos = [];
        this.loadAll();
    }

    protected pagianteFactura(data: IFactura[] | null):void{
        if(data){
            for(const d of data){
               this.facturas.push(d);             
            }
        }
    }

    protected pagianteRegistro(data: IRegistro[] | null):void{
        if(data){
            console.log("datos" + data);
            for(const d of data){

               this.registros.push(d);             
            }
        }
    }
}