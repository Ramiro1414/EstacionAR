import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { MapComponent } from './map/map.component';
import { PatronesPatentesComponent } from './patrones.patentes/patrones.patentes.component';
import { PatronesPatentesDetailComponent } from './patrones.patentes/patrones.patentes.detail.component';
import { PoligonosComponent } from './poligonos/poligonos.component'
import { PoligonosDetailComponent } from './poligonos/poligonos-detail.component';

export const routes: Routes = [
    {path: '', component: HomeComponent},
    {path: 'map', component: MapComponent},
    {path: 'patrones/patentes', component: PatronesPatentesComponent},
    {path: 'patrones/patentes/:id', component: PatronesPatentesDetailComponent},
    {path: 'poligonos', component: PoligonosComponent},
    {path: 'poligonos/:id', component: PoligonosDetailComponent}
];
