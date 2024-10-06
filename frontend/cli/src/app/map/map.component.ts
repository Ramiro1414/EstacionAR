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
  private updatedArray: any[] = []; // Array que se actualizará con las coordenadas

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
            clickable: true,
            fill: true
          }
        },
        polygon: {
          shapeOptions: {
            color: '#0000ff', // Color de la línea del polígono
            fillOpacity: 0.5 // Opacidad del relleno del polígono
          }
        },
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
      if (layer instanceof L.Polyline || layer instanceof L.Polygon) {
        this.updateArrayWithCoordinates(layer);
      }
    });

    // Escuchar el evento de edición de polilínea o polígono
    this.map.on(L.Draw.Event.EDITED, (event) => {
      const editEvent = event as L.DrawEvents.Edited;
      const layers = editEvent.layers;

      layers.eachLayer((layer: L.Layer) => {
        // Asegúrate de que el layer sea de tipo L.Polyline o L.Polygon
        if (layer instanceof L.Polyline || layer instanceof L.Polygon) {
          this.updateArrayWithCoordinates(layer);
        }
      });
    });
  }

  private updateArrayWithCoordinates(layer: L.Polyline | L.Polygon): void {
    const latLngs = layer.getLatLngs();

    // Comprobar si latLngs es un array
    if (Array.isArray(latLngs)) {
      this.updatedArray = []; // Limpiar el array antes de actualizar

      // Si es un array de arrays (en el caso de un polígono)
      latLngs.forEach(latLngArray => {
        // Verificar si latLngArray es un array
        if (Array.isArray(latLngArray)) {
          latLngArray.forEach(point => {
            if (point instanceof L.LatLng) {
              this.updatedArray.push({ lat: point.lat, lng: point.lng });
            }
          });
        } else if (latLngArray instanceof L.LatLng) {
          // Si es un solo punto
          this.updatedArray.push({ lat: latLngArray.lat, lng: latLngArray.lng });
        }
      });

      // Imprimir el array actualizado
      console.log('Array actualizado:', this.updatedArray);

      // Aquí puedes hacer la request al backend con el array actualizado si es necesario
      // this.sendToBackend(this.updatedArray);
    }
  }

  // Método para enviar al backend (implementación necesaria)
  private sendToBackend(data: any[]): void {
    // Implementa tu lógica de envío al servidor
    console.log('Enviando datos al backend:', data);
  }
}
