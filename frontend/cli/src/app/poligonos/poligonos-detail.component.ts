import { Component, AfterViewInit } from '@angular/core';
import { Poligono } from './poligono';
import { ActivatedRoute, Router } from '@angular/router';
import { PoligonoService } from './poligono.service';
import { ModalService } from '../modal/modal.service';
import { CommonModule, UpperCasePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import * as L from 'leaflet';  // Importar Leaflet
import { PolylineData } from './polylineData';
import { CustomPolyline } from './customPolyline';
import { PoligonoDTO } from './poligonoDTO';

@Component({
  selector: 'app-poligono-detail',
  standalone: true,
  imports: [UpperCasePipe, FormsModule, CommonModule],
  templateUrl: `poligonos-detail.component.html`,
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
export class PoligonosDetailComponent implements AfterViewInit {

  poligono!: Poligono;
  nuevo: boolean = false;
  private map!: L.Map; // Definir el tipo de mapa
  private drawnItems!: L.FeatureGroup; // Grupo para las líneas dibujadas
  polylinesList: PolylineData[] = []; // Inicializa el arreglo de polylines
  originalPolylinesList: PolylineData[] = [];
  private poligonoDTO!: PoligonoDTO;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: PoligonoService,
    private modalService: ModalService
  ) { }

  ngOnInit(): void {
    this.get();
  }

  ngAfterViewInit(): void {

    const mapElement = document.getElementById('map');
    if (mapElement && this.poligono && !this.map) {
      this.initializeMap();
    }

  }

  initializeMap(): void {
    this.map = L.map('map').setView([-42.77, -65.04], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(this.map);

    this.drawnItems = new L.FeatureGroup();
    this.map.addLayer(this.drawnItems);

    const drawControl = new L.Control.Draw({
      edit: {
        featureGroup: this.drawnItems
      },
      draw: {
        polyline: {
          shapeOptions: {
            color: '#ff0000',
            weight: 4
          }
        },
        polygon: false,
        circle: false,
        rectangle: false,
        marker: false,
        circlemarker: false
      }
    });
    this.map.addControl(drawControl);

    this.map.on(L.Draw.Event.CREATED, (event) => {
      const createdEvent = event as L.DrawEvents.Created;
      const layer = createdEvent.layer;

      if (layer instanceof L.Polyline) {
        const latLngs = layer.getLatLngs() as L.LatLng[];

        if (latLngs.length > 2) {
          this.map.removeLayer(layer);
        } else {
          this.drawnItems.addLayer(layer);
          this.printCoordinates(layer);

          if (latLngs.length === 2) {
            const coordinates = [
              latLngs[0].lat, latLngs[0].lng,
              latLngs[1].lat, latLngs[1].lng
            ];
            const polylineData: PolylineData = {
              id: (layer as unknown as CustomPolyline)._leaflet_id,
              coords: coordinates
            };
            this.polylinesList.push(polylineData);
          }
        }
      }
    });

    this.map.on(L.Draw.Event.EDITED, (event) => {
      const editedEvent = event as L.DrawEvents.Edited;
      const layers = editedEvent.layers;

      layers.eachLayer((layer: L.Layer) => {
        if (layer instanceof L.Polyline) {
          const latLngs = layer.getLatLngs() as L.LatLng[];

          if (latLngs.length === 2) {
            for (let i = 0; i < this.polylinesList.length; i++) {
              const polylineData = this.polylinesList[i];

              if (polylineData.id === (layer as unknown as CustomPolyline)._leaflet_id) {
                this.polylinesList[i].coords = [
                  latLngs[0].lat, latLngs[0].lng,
                  latLngs[1].lat, latLngs[1].lng
                ];
                break;
              }
            }
          }
        }
      });
    });

    this.map.on(L.Draw.Event.DELETED, (event) => {
      const deleteEvent = event as L.DrawEvents.Deleted;

      deleteEvent.layers.eachLayer((layer: L.Layer) => {
        this.polylinesList = this.polylinesList.filter(
          (polylineData) => polylineData.id !== (layer as unknown as CustomPolyline)._leaflet_id
        );
      });
    });
  }

  private printCoordinates(layer: L.Polyline | L.Polygon): void {
    const latLngs = layer.getLatLngs();

    // Comprobar si latLngs es un array
    if (Array.isArray(latLngs)) {
      latLngs.forEach(latLngArray => {
        if (Array.isArray(latLngArray)) {
          latLngArray.forEach(point => {
            if (point instanceof L.LatLng) {
              console.log(`Punto: (${point.lat}, ${point.lng})`);
            }
          });
        } else if (latLngArray instanceof L.LatLng) {
          console.log(`Punto: (${latLngArray.lat}, ${latLngArray.lng})`);
        }
      });
    }
  }

  // Método para imprimir el array de polylines
  printPolylines(): void {
    console.log('Lista de polylines de dos puntos:', this.polylinesList);
  }

  initMap(): void {
    const map = L.map('map').setView([-42.77, -65.04], 13);  // Usar coordenadas y zoom deseados

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(map);
  }

  goBack(): void {
    this.router.navigate(['poligonos']);
  }

  save(): void {
    this.poligonoDTO = <PoligonoDTO>{};
    this.poligonoDTO.id = this.poligono.id;
    this.poligonoDTO.nombre = this.poligono.nombre;
    this.poligonoDTO.precio = this.poligono.precio;
    this.poligonoDTO.polylinesList = this.polylinesList.map(polyline => ({
      id: polyline.id,
      coords: polyline.coords
    }));

    console.log('Datos del polígono a enviar:', this.poligonoDTO);

    this.service.save(this.poligonoDTO).subscribe({
      next: (dataPackage) => {
        this.poligonoDTO = <PoligonoDTO>dataPackage.data;
        this.goBack();
        this.modalService.confirm("Polígono guardado con éxito", "El polígono de estacionamiento se guardó correctamente.", "");
        console.log('Guardado exitoso:', this.poligonoDTO);
      },
      error: (error) => {
        console.error('Error al guardar el polígono:', error);
        this.modalService.error("Error al guardar", "No se pudo guardar el polígono, revise los datos e intente nuevamente.", "");
      }
    });
  }

  get(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id === 'new') {
      this.poligono = <Poligono>{};
      this.nuevo = true;
    } else {
      this.nuevo = false;
      this.poligono = <Poligono>{};

      // Obtener el polígono
      this.service.get(parseInt(id!)).subscribe((dataPackage) => {
        this.poligono = <Poligono>dataPackage.data;
      });

      // Obtener las polilíneas del polígono
      this.service.getLineasPoligono(parseInt(id!)).subscribe((dataPackage) => {
        this.poligonoDTO = <PoligonoDTO>dataPackage.data;
        this.polylinesList = this.poligonoDTO.polylinesList;
        console.log(this.polylinesList);

        // Aquí llamas a la función para dibujar las polilíneas en el mapa
        this.dibujarPolylines(this.polylinesList);
      });
    }
  }

  // Función para dibujar polilíneas en el mapa
  dibujarPolylines(polylines: PolylineData[]): void {
    for (const polyline of polylines) {
      // Asegúrate de que coords contiene dos pares de coordenadas
      const coords: L.LatLngExpression[] = [
        [polyline.coords[0], polyline.coords[1]], // Punto 1: [latitud, longitud]
        [polyline.coords[2], polyline.coords[3]]  // Punto 2: [latitud, longitud]
      ];

      // Crear la polilínea en el mapa
      L.polyline(coords, { color: '#ff0000', weight: 4, opacity: 0.5 }).addTo(this.map);
    }
  }

}