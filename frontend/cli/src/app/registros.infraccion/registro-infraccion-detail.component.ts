import { CommonModule, UpperCasePipe } from '@angular/common';
import { Component, AfterViewInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ModalService } from '../modal/modal.service';
import { RegistroInfraccionService } from './registro-infraccion.service';
import { RegistroAgenteTransito } from './registro-agente-transito';
import * as L from 'leaflet'; // Importa Leaflet

@Component({
  selector: 'app-registro-infraccion-detail',
  standalone: true,
  imports: [UpperCasePipe, FormsModule, CommonModule],
  templateUrl: `registro-infraccion-detail.component.html`,
  styles: [`
    #map {
      height: 400px; /* Altura del mapa */
      width: 100%; /* Ancho del mapa */
    }
  `]
})
export class RegistroInfraccionDetailComponent implements AfterViewInit {

  registroAgenteTransito!: RegistroAgenteTransito;
  map!: L.Map; // Variable para el mapa

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: RegistroInfraccionService,
    private modalService: ModalService,
  ) { }

  ngOnInit(): void {
    this.get();
  }

  ngAfterViewInit(): void {
    // No inicializamos el mapa aquí, solo cargamos los datos
  }

  goBack(): void {
    this.router.navigate(['registros-infracciones']);
  }

  get(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.service.getDetalleRegistroAgenteTransito(parseInt(id!)).subscribe((dataPackage) => {
      this.registroAgenteTransito = <RegistroAgenteTransito>dataPackage.data;
      this.initMap(); // Inicializa el mapa después de obtener los datos
    });
  }

  initMap(): void {
    setTimeout(() => {
      const lat = this.registroAgenteTransito.latitud;
      const lng = this.registroAgenteTransito.longitud;

      this.map = L.map('map').setView([lat, lng], 13); // Centra el mapa en las coordenadas

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '© OpenStreetMap'
      }).addTo(this.map);

      L.marker([lat, lng]).addTo(this.map)
        .bindPopup('Ubicación del Registro')
        .openPopup();
    }, 0); // Establece un tiempo de espera de 0 ms
  }
}
