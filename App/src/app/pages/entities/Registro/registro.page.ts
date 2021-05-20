
import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';




@Component({
    selector: 'app-registro',
    templateUrl: 'registro.html',
    styleUrls:['registro.scss'],
})

export class RegistroPage implements OnInit{
    isLoading = false;
   
    idFactura:number;
    

    constructor(
        protected activatedRoute: ActivatedRoute,
        protected router: Router,
        protected modalService: NgbModal,
    ){}

    
    ngOnInit(): void {
        
        
    }

  
    
  
}