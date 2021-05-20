import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import {IFactura, Factura} from '../../../../interfaces';
import { FacturaService } from 'src/app/services/Factura/factura.service';
import { VehiculoService } from 'src/app/services/Vehiculo/vehiculo.service';
import { NavController } from '@ionic/angular';

@Component({
  selector: 'app-factura-update',
  templateUrl: './factura-update.html',
})
export class FacturaUpdate implements OnInit {
  facturaId?:number;
  factura: Factura = new Factura(); 

  constructor(
    protected facturaService: FacturaService,
    protected vehiculoService: VehiculoService,
    protected activatedRoute: ActivatedRoute,
    public navController: NavController
  ) {}

  ngOnInit(): void {
    this.facturaId = this.activatedRoute.snapshot.params.idFactura
    this.loadFactura(this.facturaId);
  }

  loadFactura(id?:number){
    this.facturaService.find(id).subscribe(
      res => {
        this.factura = res.body;
      },
      (error) =>{
        console.log("error carga factura"+ error);
    }
    )
  }

  saveFactura(){
    this.facturaService.update(this.factura).subscribe(
      (res: HttpResponse<IFactura>) => {
        this.factura = res.body;
        this.previousState();
      },
      (error) =>{
        console.log(error);
    }
    )
  }

  previousState(): void {
    this.navController.navigateRoot('/vehiculo/'+this.factura.vehiculo.id);
  }
}