
import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {IFactura, IVehiculo} from '../../../interfaces';
import { FacturaService } from 'src/app/services/Factura/factura.service';



@Component({
    selector: 'app-factura',
    templateUrl: 'factura.html',
    styleUrls:['factura.scss'],
})

export class FacturaPage implements OnInit{
    isLoading = false;
    facturas: IFactura[] = [];
    idFactura:number;
    

    constructor(
        protected facturaservice: FacturaService,
        protected activatedRoute: ActivatedRoute,
        protected router: Router,
        protected modalService: NgbModal,
    ){}

    cargarFacturas(){
        this.isLoading = true;
        
        const filtros:Map<string,any> = new Map();
        filtros.set('id.equals', this.idFactura);

        console.log(filtros);

        this.facturaservice.obtenerFacturas({
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
    ngOnInit(): void {
        this.idFactura = this.activatedRoute.snapshot.params.idFactura;
        this.cargarFacturas();
        
    }

    protected pagianteFactura(data: IFactura[] | null):void{
        if(data){
            for(const d of data){
               this.facturas.push(d);             
            }
        }
    }
    
  
}