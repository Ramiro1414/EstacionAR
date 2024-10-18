import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [CommonModule], 
  template: `
    <div class="modal-header">
      <h4 class="modal-title" id="modal-title">{{title}}</h4>
      <button type="button" class="btn-close" (click)="modal.dismiss()"></button>
    </div>
    <div class="modal-body">
      <p>
        <strong>{{message}}</strong>
      </p>
      <div *ngIf="description" [innerHTML]="description"></div> <!-- Aquí se muestra la tabla -->
    </div>
    <div class="modal-footer">
      <button *ngIf="!errorModal" type="button" class="btn btn-outline-secondary" (click)="modal.dismiss()">Cancelar</button>
      <button type="button" class="btn btn-success" (click)="modal.close()">Aceptar</button>
    </div>
  `,
  styles: ``
})
export class ModalComponent {
  constructor(public modal: NgbActiveModal) {}

  @Input() title = "";
  @Input() message = "";
  @Input() description = "";
  @Input() errorModal = false; // Indicador de modal de error
}
