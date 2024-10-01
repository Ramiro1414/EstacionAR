import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { DataPackage } from '../data-package';
import { PatronPatente } from './patrones.patentes';

@Injectable({
  providedIn: 'root'
})
export class PatronesPatentesService {
  private url = 'rest/patrones/patentes';

  constructor(
    private http: HttpClient
  ) { }

  get(id: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.url}/id/${id}`);
  }

  save(patronPatente: PatronPatente): Observable<DataPackage> {
    return patronPatente.id
      ? this.http.put<DataPackage>(this.url, patronPatente) :
      this.http.post<DataPackage>(this.url, patronPatente);
  }

  remove(id: number): Observable<DataPackage> {
    return this.http.delete<DataPackage>(`${this.url}/${id}`);
  }

  byPage(page: number, size: number): Observable<DataPackage> {
    return this.http.get<DataPackage>(`${this.url}/page?page=${page - 1}&size=${size}`);
  }

}
