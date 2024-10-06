import { Component } from '@angular/core';
import { ResultsPage } from '../results-page';
import { PoligonoService } from './poligono.service';
import { ModalService } from '../modal/modal.service';
import { CommonModule } from '@angular/common';
import { PaginationComponent } from '../pagination/pagination.component';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-poligonos',
  standalone: true,
  imports: [CommonModule, RouterModule, PaginationComponent],
  templateUrl: `poligonos.component.html`,
  styles: ``
})
export class PoligonosComponent {
  
  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  ITEMS_PER_PAGE: number = 10;

  constructor(
    private service: PoligonoService,
    private modalService: ModalService
  ) { }

  ngOnInit() {
    this.getPoligonos();
  }

  getPoligonos(): void {
    this.service.byPage(this.currentPage, this.ITEMS_PER_PAGE).subscribe(
      (dataPackage) => {
        this.resultsPage = <ResultsPage>dataPackage.data;
      }
    );
  }

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    this.getPoligonos();
  }

  remove(id: number) {
    let that = this;
    this.modalService.confirm(
      "Eliminar poligono de estacionamiento",
      "¿Está seguro que desea eliminar el poligono de estacionamiento?",
      "Si elimina, el cliente no podrá revertir esta acción."
    ).then(
      function () {
        that.service.remove(id).subscribe((dataPackage) => {
          if (dataPackage.status != 200) {
            that.modalService.error(
              "Error",
              "Error al borrar el poligono de estacionamiento.",
              dataPackage.message
            );
          }
          that.getPoligonos();
        });
      }
    );
  }
  
}