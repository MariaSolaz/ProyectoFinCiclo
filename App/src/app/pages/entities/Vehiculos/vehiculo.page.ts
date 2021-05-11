
import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';


import { VehiculoService } from 'src/app/services/Vehiculo/vehiculo.service';

import {IVehiculo} from '../../../interfaces'

@Component({
    selector: 'app-vehiculo',
    templateUrl: 'vehiculo.html',
    styleUrls:['vehiculo.scss'],
})

export class VehiculoPage implements OnInit{
    
    vehiculos: IVehiculo[] =[];
    idVehiculo:number;
    isLoading = false;
    totalItems: number;

    constructor(
        protected servicioVehiculo: VehiculoService,
        protected activatedRoute: ActivatedRoute,
        protected router: Router,
        protected modalService: NgbModal,
    ){}

    cargarVehiculos(){
        this.isLoading = true;
        const filtros:Map<string,any> = new Map();
       filtros.set('id.equals', this.idVehiculo)
        this.servicioVehiculo.obtenerVehiculos({
            filter: filtros
        }).subscribe(
            (res: HttpResponse<IVehiculo[]>) => {

                this.pagianteVehiculo(res.body);
            },
            (error) =>{
                console.error(error);
            }
        );
    }

    loadAll(){
      this.cargarVehiculos();
    }
  
    ngOnInit(): void {
        this.idVehiculo = this.activatedRoute.snapshot.params.idVehiculo;
        this.loadAll();
    }

    protected pagianteVehiculo(data: IVehiculo[] | null):void{
        if(data){
            for(const d of data){
               this.vehiculos.push(d);
                
            }
        }
    }
}