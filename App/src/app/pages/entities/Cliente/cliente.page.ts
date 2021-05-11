
import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ClienteService } from 'src/app/services/Cliente/cliente.service';
import { VehiculoService } from 'src/app/services/Vehiculo/vehiculo.service';


import {ICliente} from '../../../interfaces';
import {IVehiculo} from '../../../interfaces'




@Component({
    selector: 'app-cliente',
    templateUrl: 'cliente.html',
    styleUrls:['cliente.scss'],
})

export class ClientePage implements OnInit{
    clientes: ICliente[] = [] ;
    cliente:ICliente;
    vehiculos: IVehiculo[] =[];
    nameUser?:string;

    isLoading = false;
    totalItems: number;

    idDuenyo?:IVehiculo;
  


    constructor(
        protected servicioCliente: ClienteService,
        protected servicioVehiculo: VehiculoService,
        protected activatedRoute: ActivatedRoute,
        protected router: Router,
        protected modalService: NgbModal,
    ){}

    cargarUsuario(){
        this.isLoading = true;
       const filtros:Map<string,any> = new Map();
       filtros.set('nombre.equals', this.nameUser)
       console.log(filtros)
        this.servicioCliente.obtenerClientes({
            filter: filtros
        }).subscribe(
            (res: HttpResponse<ICliente[]>) => {
                console.log(res.body);
                this.pagianteCliente(res.body);
            },
            (error) =>{
                console.error(error);
            }
        );
    }

    cargarVehiculos(){
        this.isLoading = true;
        this.servicioVehiculo.obtenerVehiculos().subscribe(
            (res: HttpResponse<IVehiculo[]>) => {

                this.pagianteVehiculo(res.body);
            },
            (error) =>{
                console.error(error);
            }
        );
    }

    loadAllclientes(){
      this.cargarUsuario();
      this.cargarVehiculos();
    }
  
    ngOnInit(): void {
        this.nameUser = this.activatedRoute.snapshot.params.nameuser;

       
        this.loadAllclientes();
    }

    

    protected pagianteCliente(data: ICliente[] | null):void{
        if(data){
            for(const d of data){
                this.cliente =  d;
                console.log(this.cliente)
                
            }
        }
    }

    protected pagianteVehiculo(data: IVehiculo[] | null):void{
        if(data){
            for(const d of data){
                if (d.id === this.cliente.vehiculo.id){
                    this.vehiculos.push(d);
                }
            }
        }
    }
}