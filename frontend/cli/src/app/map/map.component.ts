import { AfterViewInit, Component } from '@angular/core';
import L from 'leaflet';

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [],
  template: `
    <h2 class="title" style="margin-top: 1vh; margin-bottom: 2vh">Mapa de la ciudad</h2>
    <div id="map" class="map-container"></div>
  `,
  styles: [`
     .map-container {
      height: 75vh; /* Ajusta la altura del mapa */
      width: auto; /* Ajusta el ancho del mapa */
      border: 5px solid #727272; /* Añade un marco negro alrededor del mapa */
      border-radius: 15px;
      margin: 0 auto;
      margin-top: 1vh;
    }
    `]
})
export class MapComponent implements AfterViewInit {
  ngAfterViewInit(): void {
    // Inicializa el mapa cuando el componente se haya cargado completamente
    const map = L.map('map').setView([-42.77, -65.04], 13); // Coordenadas iniciales y zoom

    // Añadir capa de mapa base (OpenStreetMap)
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(map);
  }
}
