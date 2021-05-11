import { HttpClient, HttpParams, HttpClientModule, HttpResponse  } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ICliente } from 'src/app/interfaces';
import { environment } from 'src/environments/environment';
import { createRequestOption } from '../utils/request-utils';

export type EntityResponseType = HttpResponse<ICliente>;
export type EntityArrayResponseType = HttpResponse<ICliente[]>;


@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  public static API_URL = environment.apiUrl;

  constructor(public http: HttpClient) {}

 

  obtenerClientes(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICliente[]>(ClienteService.API_URL + "/clientes", { params: options, observe: 'response' });
  }



}
