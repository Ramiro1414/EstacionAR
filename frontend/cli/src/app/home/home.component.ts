import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home-component',
  standalone: true,
  imports: [],
  template: `
    <div class="px-3 py-3 pt-md-5 pb-md-4 mx-auto text-center">
      <h1 class="display-5">Bienvenido a EstacionAR</h1>
      <p class="lead">Aplicación de estacionamiento medido.</p>
    </div>
  `,
  styles: [`
    .title {
      font-size: 2.5rem; /* Cambia el tamaño del texto */
      text-align: center; /* Centra el título horizontalmente */
      margin: 20px 0; /* Añade espacio superior e inferior */
    }

    .highlight {
      color: #00bcd4; /* Aplica el color cyan solo al 'AR' */
    }
  `]
})
export class HomeComponent {

  constructor(
    private router: Router
  ){}

  verMapa() {
    this.router.navigate(['map']);
  }
  
}
