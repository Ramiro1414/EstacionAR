import { CommonModule, Location, UpperCasePipe } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ModalService } from '../modal/modal.service';
import { PatronPatente } from './patron.patente';
import { PatronesPatentesService } from './patrones.patentes.service';

@Component({
  selector: 'app-patrones-patentes-detail',
  standalone: true,
  imports: [UpperCasePipe, FormsModule, CommonModule],
  templateUrl: `patrones.patentes.detail.component.html`,
  styles: ``
})
export class PatronesPatentesDetailComponent {
  patronPatente!: PatronPatente;
  nuevo: boolean = false;
  userInput: string = '';
  generatedPattern: string = '';
  invalidInput: boolean = false;  // Nueva propiedad para validar el input

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: PatronesPatentesService,
    private modalService: ModalService,
  ) { }

  ngOnInit(): void {
    this.get();
  }

  // Método para generar la expresión regular basado en el input del usuario
  updatePattern(): void {
    this.generatedPattern = '';
    const upperInput = this.userInput.toUpperCase();

    // Verifica que solo se utilicen 'A' y '9'
    const validPattern = /^[A9]+$/;

    // Si el input es válido, genera la expresión regular
    if (validPattern.test(upperInput)) {
      this.invalidInput = false;
      let letterCount = 0;
      let digitCount = 0;

      for (let i = 0; i < upperInput.length; i++) {
        const char = upperInput[i];

        if (char === 'A') {
          letterCount++;
          if (digitCount > 0) {
            this.generatedPattern += `\\d{${digitCount}}`;
            digitCount = 0;
          }
        } else if (char === '9') {
          digitCount++;
          if (letterCount > 0) {
            this.generatedPattern += `[A-Z]{${letterCount}}`;
            letterCount = 0;
          }
        }
      }

      // Agrega cualquier grupo pendiente
      if (letterCount > 0) {
        this.generatedPattern += `[A-Z]{${letterCount}}`;
      } else if (digitCount > 0) {
        this.generatedPattern += `\\d{${digitCount}}`;
      }
    } else {
      // Si el input es inválido, muestra un mensaje de error
      this.generatedPattern = 'Error: solo se permiten los caracteres "A" para letras y "9" para dígitos.';
      this.invalidInput = true;
    }
  }

  save(): void {
    if (!this.invalidInput) {
      this.patronPatente.expresionRegularPatente = this.generatedPattern;

      this.service.save(this.patronPatente).subscribe(dataPackage => {
        if (dataPackage.status != 200) {
          this.modalService.error(
            "Error",
            "Error al guardar el patrón de patentes.",
            dataPackage.message
          );
        } else {
          this.patronPatente = <PatronPatente>dataPackage.data;
          this.router.navigate(['patrones/patentes']);
        }
      });
    }
  }

  traducirExpresionRegular(regex: string): string {
    if (regex.includes('[A-Z]') || regex.includes('\\d')) {
      return regex.replace(/\[A-Z\]\{(\d+)\}/g, '$1 letras mayúsculas ')
                  .replace(/\\d\{(\d+)\}/g, '$1 dígitos ');
    }
    return regex;
  }

  goBack(): void {
    this.router.navigate(['patrones/patentes']);
  }

  get(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id === 'new') {
      this.patronPatente = <PatronPatente>{};
      this.nuevo = true;
    } else {
      this.nuevo = false;
      this.service.get(parseInt(id!)).subscribe((dataPackage) => {
        this.patronPatente = <PatronPatente>dataPackage.data;
        this.userInput = ''; // Limpiar el input inicial
        this.updatePattern();
      });
    }
  }
}
