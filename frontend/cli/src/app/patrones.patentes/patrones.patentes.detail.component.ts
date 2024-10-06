import { CommonModule, Location, UpperCasePipe } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ModalService } from '../modal/modal.service';
import { PatronPatente } from './patrones.patentes';
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
  regexArray: { regex: string, repetitions: number }[] = [];
  generatedPattern: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: PatronesPatentesService,
    private modalService: ModalService,
  ) { }

  ngOnInit(): void {
    this.get();
  }

  addRegexItem(): void {
    this.regexArray.push({ regex: '', repetitions: 1 });
    this.updatePattern();
  }

  removeRegexItem(index: number): void {
    this.regexArray.splice(index, 1);
    this.updatePattern();
  }

  // Actualizar la expresión regular combinada
  updatePattern(): void {
    this.generatedPattern = this.regexArray.map(item => {
      // Escapar la barra invertida para que se guarde correctamente
      const escapedRegex = item.regex.replace(/\\/g, '\\');
      return `${escapedRegex}{${item.repetitions}}`;
    }).join('');
  }

  save(): void {
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

  traducirExpresionRegular(regex: string): string {
    const patrones = [
      { regex: /\[A-Z\]\{(\d+)\}/g, descripcion: '$1 letras mayúsculas ' },
      { regex: /\\d\{(\d+)\}/g, descripcion: '$1 dígitos ' },
      { regex: /\\d/g, descripcion: '1 dígito ' },
      { regex: /\[A-Z\]/g, descripcion: '1 letra mayúscula ' },
    ];

    let descripcion = regex;
    patrones.forEach(patron => {
      descripcion = descripcion.replace(patron.regex, patron.descripcion);
    });

    return descripcion.replace(/\s+/g, ' ').replace(/\\/, '').trim();
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
        this.regexArray = this.parseExpressionToArray(this.patronPatente.expresionRegularPatente);
        this.updatePattern();
      });
    }
  }

  parseExpressionToArray(expression: string): { regex: string, repetitions: number }[] {
    const pattern = /(\[A-Z\]|\\d)\{(\d+)\}/g;
    let match;
    const result: { regex: string, repetitions: number }[] = [];

    while ((match = pattern.exec(expression)) !== null) {
      result.push({ regex: match[1], repetitions: parseInt(match[2], 10) });
    }
    return result;
  }
}
