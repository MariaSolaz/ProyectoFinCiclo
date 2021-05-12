
import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'app-cliente',
    templateUrl: 'cliente.html',
    styleUrls:['cliente.scss'],
})

export class FacturaPage implements OnInit{
    
    constructor(
        protected activatedRoute: ActivatedRoute,
        protected router: Router,
        protected modalService: NgbModal,
    ){}

    ngOnInit(): void {
        
    }
}