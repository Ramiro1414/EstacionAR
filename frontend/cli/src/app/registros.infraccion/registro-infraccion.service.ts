import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DataPackage } from '../data-package';

@Injectable({
  providedIn: 'root'
})
export class RegistroInfraccionService {
  private url = 'rest/registros-infracciones';
  private apiUrl = `https://nominatim.openstreetmap.org/reverse`;

  constructor(
    private http: HttpClient
  ) { }

  get(id: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.url}/id/${id}`);
  }

  getDetalleRegistroAgenteTransito(id: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.url}/detalle/registro-agente-transito/id/${id}`);
  }

  byPage(page: number, size: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.url}/page?page=${page - 1}&size=${size}`);
  }

  getAddressFromCoordinates(lat: number, lng: number) {
    return this.http.get(`${this.apiUrl}?lat=${lat}&lon=${lng}&format=json`);
  }
}
