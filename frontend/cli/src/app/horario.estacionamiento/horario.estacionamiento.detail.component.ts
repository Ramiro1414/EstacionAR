import { Component, OnInit } from '@angular/core';
import { HorarioEstacionamiento } from './horario.estacionamiento';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HorarioEstacionamientoService } from './horario.estacionamiento.service';
import { ModalService } from '../modal/modal.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HoraInicioHoraFin } from './hora.inicio.hora.fin';

@Component({
  selector: 'app-horario.estacionamiento.detail',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: `horario.estacionamiento.detail.component.html`,
  styles: `
  .form-container {
    padding: 20px;
  }
  
  .form-group {
    margin-bottom: 20px;
  }
  `
})
export class HorarioEstacionamientoDetailComponent implements OnInit {
  horarioEstacionamiento!: HorarioEstacionamiento;
  
  // Variables para el día y mes de inicio/fin
  diaInicio!: string;
  mesInicio!: string;
  diaFin!: string;
  mesFin!: string;

  // Listas de días y meses
  dias: string[] = Array.from({ length: 31 }, (_, i) => (i + 1).toString().padStart(2, '0'));
  meses: { name: string, value: string }[] = [
    { name: 'Enero', value: '01' }, { name: 'Febrero', value: '02' }, { name: 'Marzo', value: '03' },
    { name: 'Abril', value: '04' }, { name: 'Mayo', value: '05' }, { name: 'Junio', value: '06' },
    { name: 'Julio', value: '07' }, { name: 'Agosto', value: '08' }, { name: 'Septiembre', value: '09' },
    { name: 'Octubre', value: '10' }, { name: 'Noviembre', value: '11' }, { name: 'Diciembre', value: '12' }
  ];

  nuevo: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: HorarioEstacionamientoService,
    private modalService: ModalService,
  ) { }

  ngOnInit(): void {
    this.get();
  }

  get(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id === 'new') { 
      this.horarioEstacionamiento = <HorarioEstacionamiento>{horariosDelDia: <HoraInicioHoraFin[]>[]};
      this.nuevo = true;
    } else {
      this.nuevo = false;
      this.service.get(parseInt(id!)).subscribe((dataPackage) => {
        this.horarioEstacionamiento = <HorarioEstacionamiento>dataPackage.data;
        
        // Descomponer las fechas en día y mes
        [this.mesInicio, this.diaInicio] = this.horarioEstacionamiento.fechaInicio.split('-');
        [this.mesFin, this.diaFin] = this.horarioEstacionamiento.fechaFin.split('-');
      });
    }
  }

  save(): void {
    if (!this.checkNoOverlap(this.horarioEstacionamiento.horariosDelDia)) {
      this.modalService.error("Error", "Error al guardar el horario de estacionamiento", "Los horarios del dia estan superpuestos.");
      return;
    }

    this.horarioEstacionamiento.fechaInicio = `${this.mesInicio}-${this.diaInicio}`;
    this.horarioEstacionamiento.fechaFin = `${this.mesFin}-${this.diaFin}`;
    
    this.service.save(this.horarioEstacionamiento).subscribe(dataPackage => {
      if (dataPackage.status != 200) {
        this.modalService.error(
          "Error",
          "Error al guardar el horario de estacionamiento.",
          dataPackage.message
        );
      } else {
        this.router.navigate(['horarios/estacionamiento']);
      }
    });
  }

  addHora() :void{
    this.horarioEstacionamiento.horariosDelDia.push({
      id: 0,
      horaInicio: '',
      horaFin: ''
    });
  }

  removeHora(horaDia : HoraInicioHoraFin) :void{
    this.modalService.confirm(
      "Eliminar la tarea",
      "¿Esta seguro que desea borrar esta tarea?",
      "El cambio no se confirmara hasta que no guarde el producto.")
      .then(
        (_) => {
          let horariosDelDia = this.horarioEstacionamiento.horariosDelDia;
          horariosDelDia.splice(horariosDelDia.indexOf(horaDia), 1);
        }
      )
  }

  checkNoOverlap(horarios: { horaInicio: string, horaFin: string }[]): boolean {
    for (let i = 0; i < horarios.length; i++) {
      for (let j = i + 1; j < horarios.length; j++) {
        const horaInicio1 = this.parseTime(horarios[i].horaInicio);
        const horaFin1 = this.parseTime(horarios[i].horaFin);
        const horaInicio2 = this.parseTime(horarios[j].horaInicio);
        const horaFin2 = this.parseTime(horarios[j].horaFin);
  
        // Verificar si los horarios se superponen
        if (!(horaFin1 <= horaInicio2 || horaFin2 <= horaInicio1)) {
          // Si los horarios se superponen, retornamos false
          return false;
        }
      }
    }
    // Si no se detectan superposiciones, retornamos true
    return true;
  }
  
  parseTime(time: string): Date {
    const [hours, minutes] = time.split(':').map(Number);
    const date = new Date();
    date.setHours(hours, minutes, 0, 0);
    return date;
  }
  
  goBack(): void {
    this.router.navigate(['horarios/estacionamiento']);
  }
}
