import { HttpClient, HttpParams, HttpClientModule, HttpResponse  } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IFactura } from 'src/app/interfaces';
import { environment } from 'src/environments/environment';
import { createRequestOption } from '../utils/request-utils';

export type EntityResponseType = HttpResponse<IFactura>;
export type EntityArrayResponseType = HttpResponse<IFactura[]>;

@Injectable({
  providedIn: 'root'
})
export class FacturaService {
  public static API_URL = environment.apiUrl;

  constructor(public http: HttpClient) {}

  obtenerFacturas(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFactura[]>(FacturaService.API_URL + "/facturas", { params: options, observe: 'response' });
  }

}
