import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DataPackage } from '../data-package';
import { Poligono } from './poligono';
import { PoligonoDTO } from './poligonoDTO';

@Injectable({
  providedIn: 'root'
})
export class PoligonoService {
  private url = 'rest/poligonos'

  constructor(
    private http: HttpClient
  ) { }

  get(id: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.url}/id/${id}`);
  }

  getLineasPoligono(id: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.url}/id/${id}/lineas-poligono`)
  }

  save(poligonoDTO: PoligonoDTO): Observable<DataPackage> {
    return poligonoDTO.id
      ? this.http.put<DataPackage>(this.url, poligonoDTO) :
      this.http.post<DataPackage>(this.url, poligonoDTO);
  }

  byPage(page: number, size: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.url}/page?page=${page - 1}&size=${size}`);
  }

  remove(id: number): Observable<DataPackage> {
    return this.http.delete<DataPackage>(`${this.url}/${id}`);
  }
}
