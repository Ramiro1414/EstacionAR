import { Component } from '@angular/core';
import { PaginationComponent } from '../pagination/pagination.component';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ResultsPage } from '../results-page';
import { RegistroInfraccionService } from './registro-infraccion.service';
import { ModalService } from '../modal/modal.service';

@Component({
  selector: 'app-registros.infraccion',
  standalone: true,
  imports: [CommonModule, RouterModule, PaginationComponent],
  templateUrl: `registros.infraccion.component.html`,
  styles: ``
})
export class RegistrosInfraccionComponent {

  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  ITEMS_PER_PAGE: number = 10;
  
  constructor(
    private service: RegistroInfraccionService,
    private modalService: ModalService
  ) { }

  ngOnInit() {
    this.getRegistrosInfraccion();
  }

  getRegistrosInfraccion(): void {
    this.service.byPage(this.currentPage, this.ITEMS_PER_PAGE).subscribe(
      (dataPackage) => {
        this.resultsPage = <ResultsPage>dataPackage.data;
      }
    );
  }

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    this.getRegistrosInfraccion();
  }

}
