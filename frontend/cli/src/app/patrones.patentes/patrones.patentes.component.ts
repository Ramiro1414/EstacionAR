import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { PaginationComponent } from '../pagination/pagination.component';
import { ResultsPage } from '../results-page';
import { PatronesPatentesService } from './patrones.patentes.service';
import { ModalService } from '../modal/modal.service';

@Component({
  selector: 'app-patrones.patentes',
  standalone: true,
  imports: [CommonModule, RouterModule, PaginationComponent],
  templateUrl: `patrones.patentes.component.html`,
  styles: ``
})
export class PatronesPatentesComponent {

  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  ITEMS_PER_PAGE: number = 10;

  constructor(
    private service: PatronesPatentesService,
    private modalService: ModalService
  ) { }

  ngOnInit() {
    this.getPatronesPatentes();
  }

  getPatronesPatentes(): void {
    this.service.byPage(this.currentPage, this.ITEMS_PER_PAGE).subscribe(
      (dataPackage) => {
        this.resultsPage = <ResultsPage>dataPackage.data;
      }
    );
  }

  remove(id: number) {
    let that = this;
    this.modalService.confirm(
      "Eliminar patrón de patente",
      "¿Está seguro que desea eliminar el patrón de patente?",
      "Si lo elimina no podrá revertir esta acción."
    ).then(
      function () {
        that.service.remove(id).subscribe((dataPackage) => {
          if (dataPackage.status != 200) {
            that.modalService.error(
              "Error",
              "Error al borrar el patrón de patentes.",
              dataPackage.message
            );
          }
          that.getPatronesPatentes();
        });
      }
    );
  }

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    this.getPatronesPatentes();
  }

  traducirExpresionRegular(regex: string): string {
    const patrones = [
      { regex: /\[A-Z\]\{(\d+)\}/g, descripcion: '$1 letras mayúsculas ' },
      { regex: /\\d\{(\d+)\}/g, descripcion: '$1 dígitos ' },
      { regex: /\\d/g, descripcion: '1 dígito ' },
      { regex: /\[A-Z\]/g, descripcion: '1 letra mayúscula ' },
    ];

    let descripcion = regex;

    patrones.forEach(patron => {
      descripcion = descripcion.replace(patron.regex, patron.descripcion);
    });

    return descripcion.replace(/\s+/g, ' ').replace(/\\/, '').trim();
  }

  // Método para generar un ejemplo basado en la expresión regular
  generarEjemploPatente(regex: string): string {
    let ejemplo = '';

    // Detecta patrones de letras mayúsculas y dígitos, y los reemplaza por ejemplos
    const letterPattern = /\[A-Z\]\{(\d+)\}/g;
    const digitPattern = /\\d\{(\d+)\}/g;
    const singleLetterPattern = /\[A-Z\]/g;
    const singleDigitPattern = /\\d/g;

    ejemplo = regex
      .replace(letterPattern, (match, p1) => 'A'.repeat(parseInt(p1, 10))) // Reemplaza [A-Z]{x} por 'A' repetido x veces
      .replace(digitPattern, (match, p1) => '9'.repeat(parseInt(p1, 10)))  // Reemplaza \d{x} por '9' repetido x veces
      .replace(singleLetterPattern, 'A')  // Reemplaza [A-Z] por una 'A'
      .replace(singleDigitPattern, '9');  // Reemplaza \d por un '9'

    return ejemplo;
  }
}
