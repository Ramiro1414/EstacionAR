import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DataPackage } from '../data-package';
import { Poligono } from './poligono';

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

  save(poligono: Poligono): Observable<DataPackage> {
    return poligono.id
      ? this.http.put<DataPackage>(this.url, poligono) :
      this.http.post<DataPackage>(this.url, poligono);
  }

  byPage(page: number, size: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.url}/page?page=${page - 1}&size=${size}`);
  }

  remove(id: number): Observable<DataPackage> {
    return this.http.delete<DataPackage>(`${this.url}/${id}`);
  }
}
