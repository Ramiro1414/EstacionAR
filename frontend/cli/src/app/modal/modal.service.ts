import { Injectable } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalComponent } from './modal.component';

@Injectable({
  providedIn: 'root'
})
export class ModalService {

  constructor(private modalService: NgbModal) { }

  confirm(title: string, message: string, description: string): Promise<any> {
    const modal = this.modalService.open(ModalComponent);
    modal.componentInstance.title = title;
    modal.componentInstance.message = message;
    modal.componentInstance.description = description;
    modal.componentInstance.errorModal = false;
    return modal.result;
  }

  error(title: string, message: string, description: string): Promise<any> {
    const modal = this.modalService.open(ModalComponent);
    modal.componentInstance.title = title;
    modal.componentInstance.message = message;
    modal.componentInstance.description = description;
    modal.componentInstance.errorModal = true; 
    return modal.result;
  }

  horarioInfo(title: string, message: string, horarioData: any[]): Promise<any> {
    const modal = this.modalService.open(ModalComponent);
    modal.componentInstance.title = title;
    modal.componentInstance.message = message;
    modal.componentInstance.description = this.generateHorarioTable(horarioData);
    modal.componentInstance.errorModal = true; 
    return modal.result;
  }
  
  private generateHorarioTable(horarioData: any[]): string {
    let tableHtml = `<table class="table table-striped table-sm" >
                       <thead>
                         <tr>
                           <th>Hora de Inicio</th>
                           <th>Hora de Fin</th>
                         </tr>
                       </thead>
                       <tbody>`;
    
    horarioData.forEach(horario => {
      tableHtml += `<tr>
                       <td>${horario.horaInicio}</td>
                       <td>${horario.horaFin}</td>
                     </tr>`;
    });
    
    tableHtml += `</tbody></table>`;
    return tableHtml;
  }
  
}
