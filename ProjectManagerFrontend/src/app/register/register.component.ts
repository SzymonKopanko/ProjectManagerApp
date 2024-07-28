import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  username: string = '';
  password: string = '';
  email: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    this.authService.register(this.username, this.password, this.email).subscribe(
      response => {
        console.log('Registration successful', response);
        this.router.navigate(['/projects']);
      },
      error => {
        console.log('Registration failed', error);
      }
    );
  }
}
