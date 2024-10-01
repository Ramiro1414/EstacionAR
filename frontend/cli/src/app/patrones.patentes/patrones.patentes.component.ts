import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { PaginationComponent } from '../pagination/pagination.component';
import { ResultsPage } from '../results-page';
import { PatronesPatentesService} from './patrones.patentes.service';
import { ModalService } from '../modal/modal.service';

@Component({
  selector: 'app-patrones.patentes',
  standalone: true,
  imports: [CommonModule, RouterModule, PaginationComponent],
  templateUrl: `patrones.patentes.html`,
  styles: ``
})
export class PatronesPatentesComponent {

  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  ITEMS_PER_PAGE: number = 10;

  constructor(
    private service : PatronesPatentesService,
    private modalService: ModalService
  ){}

  ngOnInit() {
    this.getPatronesPatentes();
  }

  getPatronesPatentes(): void {
    this.service.byPage(this.currentPage, this.ITEMS_PER_PAGE).subscribe(
      (dataPackage) => {  this.resultsPage = <ResultsPage>dataPackage.data; }
    );
  }

  remove(id: number){
    let that = this;
    this.modalService.confirm(
      "Eliminar patron de patente",
      "¿Está seguro que desea eliminar el patrón de patente?",
      "Si elimina el cliente no podrá revertir esta acción."
    ).then(
      function () {
        that.service.remove(id).subscribe((dataPackage) => {
          if (dataPackage.status != 200) {
            that.modalService.error(
              "Error",
              "Error al borrar el patrón de patentes.",
              dataPackage.message
            )
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
}
