import { AfterViewInit, Component } from '@angular/core';
import L from 'leaflet';
import 'leaflet-draw';
import 'leaflet-draw/dist/leaflet.draw.css'; // Importa los estilos

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
      user-select: none; /* Evitar la selección de texto */
    }
  `]
})
export class MapComponent implements AfterViewInit {
  private map!: L.Map; // Definir el tipo de mapa
  private drawnItems!: L.FeatureGroup; // Grupo para las líneas dibujadas

  ngAfterViewInit(): void {
    // Inicializa el mapa
    this.map = L.map('map').setView([-42.77, -65.04], 13); // Coordenadas iniciales y zoom

    // Añadir capa de mapa base (OpenStreetMap)
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(this.map);

    // Crea una capa para los elementos dibujados
    this.drawnItems = new L.FeatureGroup();
    this.map.addLayer(this.drawnItems);

    // Inicializa el control de dibujo
    const drawControl = new L.Control.Draw({
      edit: {
        featureGroup: this.drawnItems
      },
      draw: {
        polyline: {
          shapeOptions: {
            color: '#ff0000', // Color de la línea
            weight: 4, // Grosor de la línea
            clickable: true
          }
        },
        polygon: false,
        rectangle: false,
        circle: false, // Desactiva la opción de dibujar círculos
        circlemarker: false, // Desactiva la opción de dibujar marcadores circulares
        marker: false // Desactiva la opción de dibujar marcadores
      }
    });
    this.map.addControl(drawControl);

    // Escuchar el evento de creación de una línea
    this.map.on(L.Draw.Event.CREATED, (event) => {
      const createdEvent = event as L.DrawEvents.Created;
      const layer = createdEvent.layer;
      this.drawnItems.addLayer(layer);

      // Obtener y mostrar las coordenadas de la línea dibujada
      if (layer instanceof L.Polyline) {
        const latLngs = layer.getLatLngs();

        if (Array.isArray(latLngs)) {
          for (let i = 0; i < latLngs.length; i += 2) {
            const start = latLngs[i] as L.LatLng;
            const end = latLngs[i + 1] ? (latLngs[i + 1] as L.LatLng) : null;
            if (end) {
              console.log(`Puntos: Inicio: (${start.lat}, ${start.lng}); Fin: (${end.lat}, ${end.lng})`);
            }
          }
        }
      }
    });
  }
}
