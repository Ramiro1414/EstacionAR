import { CommonModule, UpperCasePipe } from '@angular/common';
import { Component, AfterViewInit, OnInit, OnDestroy } from '@angular/core';
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
export class RegistroInfraccionDetailComponent implements AfterViewInit, OnInit, OnDestroy {

  registroAgenteTransito!: RegistroAgenteTransito;
  map!: L.Map; // Variable para el mapa
  imageUrl: string | null = null; // Propiedad para almacenar la URL de la imagen

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: RegistroInfraccionService,
    private modalService: ModalService,
  ) { 

    const dummyImageData = new Uint8Array([/* Aquí van los bytes de la imagen */]);
    const blob = new Blob([dummyImageData], { type: 'image/jpeg' });

  }

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
        console.log('Data Package:', dataPackage); // Log para verificar la respuesta completa
        this.registroAgenteTransito = <RegistroAgenteTransito>dataPackage.data;

        // Verifica si el campo 'foto' es una cadena y lo convierte a Blob
        if (typeof this.registroAgenteTransito.foto === 'string') {
            const base64String = this.registroAgenteTransito.foto.replace(/^data:image\/(png|jpeg);base64,/, ""); // Asegúrate de que la cadena esté limpia
            this.registroAgenteTransito.foto = this.base64ToBlob(base64String, 'image/jpeg'); // Cambia 'image/jpeg' por el tipo correcto si es necesario
        }

        console.log('Registro Agente Transito:', this.registroAgenteTransito); // Log para verificar el objeto completo

        if (this.registroAgenteTransito.foto instanceof Blob) {
            this.generateImageUrl();
        } else {
            console.warn('No se recibió un Blob en foto');
        }

        this.initMap();
    });
  }

  // Función para convertir Base64 a Blob
  base64ToBlob(base64: string, contentType: string): Blob {
    // Decodifica la cadena Base64
    const byteCharacters = atob(base64);
    const byteNumbers = new Array(byteCharacters.length);

    // Convierte cada carácter a su código de carácter
    for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
    }

    // Crea un array de bytes a partir de los números
    const byteArray = new Uint8Array(byteNumbers);

    // Devuelve un Blob
    return new Blob([byteArray], { type: contentType });
  }

  generateImageUrl() {
    if (this.registroAgenteTransito && this.registroAgenteTransito.foto instanceof Blob) {
        this.imageUrl = URL.createObjectURL(this.registroAgenteTransito.foto);
        console.log('Image URL:', this.imageUrl); // Para verificar la URL generada
    }
  }

  ngOnDestroy() {
    if (this.imageUrl) {
        URL.revokeObjectURL(this.imageUrl); // Libera la URL si existe
    }
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