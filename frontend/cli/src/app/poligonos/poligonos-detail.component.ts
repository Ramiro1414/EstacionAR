import { Component, AfterViewInit } from '@angular/core';
import { Poligono } from './poligono';
import { ActivatedRoute, Router } from '@angular/router';
import { PoligonoService } from './poligono.service';
import { ModalService } from '../modal/modal.service';
import { CommonModule, UpperCasePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Punto } from './punto';
import * as L from 'leaflet';
import 'leaflet-draw';

@Component({
  selector: 'app-poligono-detail',
  standalone: true,
  imports: [UpperCasePipe, FormsModule, CommonModule],
  templateUrl: './poligonos-detail.component.html',
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
  puntos: Punto[] = [];
  map!: L.Map;
  mapInitialized: boolean = false;
  drawnItems = new L.FeatureGroup(); // Crear el FeatureGroup globalmente

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: PoligonoService,
    private modalService: ModalService
  ) { }

  ngAfterViewChecked(): void {
    if (!this.mapInitialized) {
      const mapContainer = document.getElementById('map');
      if (mapContainer) {
        this.initMap();
        this.mapInitialized = true; // Marcamos que el mapa ya ha sido inicializado
        if (this.puntos.length !== 0) {
          this.dibujarPoligono(this.puntos);
        }
      }
    }
  }

  ngOnInit(): void {
    this.get();
  }

  ngAfterViewInit(): void {
    // El mapa no se inicializa aquí directamente
  }

  initMap(): void {
    if (this.map) {
      return; // Evitar inicializar el mapa más de una vez
    }

    const mapContainer = document.getElementById('map');
    if (!mapContainer) {
      console.error('Map container not found');
      return;
    }

    this.map = L.map(mapContainer).setView([-42.77, -65.04], 13);

    // Cargar una capa de mapa
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '© OpenStreetMap'
    }).addTo(this.map);

    // Añadir drawnItems al mapa
    this.map.addLayer(this.drawnItems);

    // Configurar el control de dibujo
    const drawControl = new L.Control.Draw({
      edit: {
        featureGroup: this.drawnItems
      },
      draw: {
        polygon: {},
        polyline: false,
        rectangle: false,
        circle: false,
        marker: false
      }
    });
    this.map.addControl(drawControl);

    // Evento al finalizar el dibujo de un polígono
    this.map.on(L.Draw.Event.CREATED, (event: any) => {
      const layer = event.layer;
      this.drawnItems.addLayer(layer);

      // Obtener los puntos del polígono
      this.actualizarPuntos(layer);
    });

    // Evento al editar un polígono existente
    this.map.on(L.Draw.Event.EDITED, (event: any) => {
      const layers = event.layers;
      layers.eachLayer((layer: any) => {
        // Actualizar los puntos del polígono editado
        this.actualizarPuntos(layer);
      });
    });
  }

  actualizarPuntos(layer: any): void {
    const latlngs = layer.getLatLngs()[0];
    this.puntos = latlngs.map((latlng: L.LatLng, index: number) => ({
      id: index + 1, // Mantener un ID secuencial
      latitud: latlng.lat,
      longitud: latlng.lng
    }));

    console.log(this.puntos); // Verificar los puntos actualizados en la consola
  }

  get(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id === 'new') {
      this.poligono = <Poligono>{};
      this.nuevo = true;
    } else {
      this.nuevo = false;
      this.service.get(parseInt(id!)).subscribe((dataPackage) => {
        this.poligono = <Poligono>dataPackage.data;
        this.puntos = this.poligono.puntos;
        if (this.mapInitialized) {
          this.dibujarPoligono(this.puntos);
        }
      });
    }
  }

  dibujarPoligono(puntos: Punto[]): void {
    if (!this.map || !puntos || puntos.length === 0) {
      return; // Si el mapa no está inicializado o no hay puntos, salir
    }

    // Convertir los puntos (Punto[]) en un array de L.LatLng para Leaflet
    const latLngs = puntos.map(punto => L.latLng(punto.latitud, punto.longitud));

    // Crear el polígono con los puntos
    const poligonoLayer = L.polygon(latLngs, { color: 'blue' });

    // Agregar el polígono al FeatureGroup de edición
    this.drawnItems.addLayer(poligonoLayer);

    // Ajustar la vista del mapa para que se centre en el polígono
    this.map.fitBounds(poligonoLayer.getBounds());
  }
  

  goBack(): void {
    this.router.navigate(['poligonos']);
  }

  save(): void {

    this.puntos.forEach(punto => {
      punto.id = 0;
    });

    this.poligono.puntos = this.puntos;

    this.service.save(this.poligono).subscribe(dataPackage => {
      
      console.log(this.poligono);

      if (dataPackage.status != 200) {
        this.modalService.error(
          "Error",
          "Error al guardar el poligono.",
          dataPackage.message
        );
      } else if (dataPackage.status == 200) {
        this.modalService.confirm(
          "OK",
          "Poligono de estacionamiento guardado correctamente.",
          dataPackage.message
        );
        this.goBack();
      }
    });
  }

  print(): void {

  }
}
