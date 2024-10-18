import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { DataPackage } from '../data-package';
import { HorarioEstacionamiento } from './horario.estacionamiento';

@Injectable({
  providedIn: 'root'
})
export class HorarioEstacionamientoService {

  private url = 'rest/horarios/estacionamiento';

  constructor(
    private http: HttpClient
  ) { }

  get(id: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.url}/id/${id}`);
  }

  getHorariosDelDia(id: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.url}/horariosDia/${id}`);
  }

  save(horarioEstacionamiento: HorarioEstacionamiento): Observable<DataPackage> {
    return horarioEstacionamiento.id
      ? this.http.put<DataPackage>(this.url, horarioEstacionamiento) :
      this.http.post<DataPackage>(this.url, horarioEstacionamiento);
  }

  remove(id: number): Observable<DataPackage> {
    return this.http.delete<DataPackage>(`${this.url}/${id}`);
  }

  byPage(page: number, size: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.url}/page?page=${page - 1}&size=${size}`);
  }


}
