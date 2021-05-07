import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ApiService } from 'src/app/services/api/api.service';
import {ICliente} from '../../../interfaces';


@Component({
    selector: 'app-cliente',
    templateUrl: 'cliente.html',
    styleUrls:['cliente.scss'],
})

export class ClientePage implements OnInit{
    cliente: ICliente[] = [];
    isLoading = false;
    totalItems: number;


    constructor(
        protected servicio: ApiService,
        protected activatedRoute: ActivatedRoute,
        protected router: Router,
        protected modalService: NgbModal,
    ){}


  
    ngOnInit(): void {
        //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
        //Add 'implements OnInit' to the class.
        
    }
}