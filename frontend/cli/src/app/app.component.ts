import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,

  imports: [RouterOutlet, CommonModule, RouterModule],
  templateUrl: 'app.component.html',
  styleUrl: 'app.component.css',
  styles: [`
  .loading-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(255, 255, 255, 0.8);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
  }
  .loading .header, .loading .container.white-div {
    pointer-events: none;
  }
  `],
})
export class AppComponent {
  loading: boolean = false;

  constructor(private router: Router) {}

  setLoading(isLoading: boolean) {
    this.loading = isLoading;
  }
}