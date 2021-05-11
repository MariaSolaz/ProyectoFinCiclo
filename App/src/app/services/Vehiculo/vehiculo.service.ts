import { HttpClient, HttpParams, HttpClientModule, HttpResponse  } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IVehiculo } from 'src/app/interfaces';
import { environment } from 'src/environments/environment';
import { createRequestOption } from '../utils/request-utils';

export type EntityResponseType = HttpResponse<IVehiculo>;
export type EntityArrayResponseType = HttpResponse<IVehiculo[]>;

@Injectable({
  providedIn: 'root'
})
export class VehiculoService {
  public static API_URL = environment.apiUrl;

  constructor(public http: HttpClient) {}

  obtenerVehiculos(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVehiculo[]>(VehiculoService.API_URL + "/vehiculos", { params: options, observe: 'response' });
  }

}
