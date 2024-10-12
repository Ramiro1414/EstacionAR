import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DataPackage } from '../data-package';
import axios from 'axios';

@Injectable({
  providedIn: 'root'
})
export class RegistroInfraccionService {
  private url = 'rest/registros-infracciones';

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

  async obtenerUbicacion(latitud: number, longitud: number): Promise<string> {
    try {
        const response = await axios.get('https://nominatim.openstreetmap.org/reverse', {
            params: {
                lat: latitud,
                lon: longitud,
                format: 'json',
            }
        });

        return response.data.address.road + (response.data.address.house_number ? " " + response.data.address.house_number : "");
    } catch (error) {
        console.error('Error obteniendo la ubicación:', error);
        return 'Ubicación no disponible';
    }
}

}
