import { Component } from '@angular/core';
import { ResultsPage } from '../results-page';
import { HorarioEstacionamientoService } from './horario.estacionamiento.service';
import { ModalService } from '../modal/modal.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PaginationComponent } from '../pagination/pagination.component';
import { HoraInicioHoraFin } from './hora.inicio.hora.fin';

@Component({
  selector: 'app-horario.estacionamiento',
  standalone: true,
  imports: [CommonModule, RouterModule, PaginationComponent],
  templateUrl: `horario.estacionamiento.component.html`,
  styles: ``
})
export class HorarioEstacionamientoComponent { 

  resultsPage: ResultsPage = <ResultsPage>{};
  currentPage: number = 1;
  ITEMS_PER_PAGE: number = 10;

  constructor(
    private service: HorarioEstacionamientoService,
    private modalService: ModalService
  ) { }


  ngOnInit() {
    this.get();
  }

  get(): void {
    this.service.byPage(this.currentPage, this.ITEMS_PER_PAGE).subscribe(
      (dataPackage) => {
        this.resultsPage = <ResultsPage>dataPackage.data;
      }
    );
  }

  remove(id: number) {
    let that = this;
    this.modalService.confirm(
      "Eliminar horario de estacionamiento",
      "¿Está seguro que desea eliminar el horario de estacionamiento?",
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
          that.get();
        });
      }
    );
  }
  
  getFechaLegible(fecha: string): string {
    const meses = [
        'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
        'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
    ];

    const [mes, dia] = fecha.split('-');
    return `${dia} de ${meses[parseInt(mes) - 1]}`; // Restar 1 porque los índices del array comienzan en 0
  }

  showInfo(id: number) {
    this.service.getHorariosDelDia(id).subscribe(dataPackage => {
      let horarios: HoraInicioHoraFin[] = <HoraInicioHoraFin[]> dataPackage.data; 
  
      // Pasar los datos al modal después de recibirlos
      this.modalService.horarioInfo("Información de Horarios", "Estos son los horarios disponibles:", horarios); 
    }, error => {
      console.error('Error al obtener los horarios', error);
      // Opcional: mostrar un modal de error si la llamada falla
      this.modalService.error("Error", "No se pudieron cargar los horarios", "");
    });
  }
  

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    this.get();
  }

}
