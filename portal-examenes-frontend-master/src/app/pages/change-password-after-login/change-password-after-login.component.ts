import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import { UserService } from 'src/app/services/user.service';


@Component({
  selector: 'app-change-password-after-login',
  templateUrl: './change-password-after-login.component.html',
  styleUrls: ['./change-password-after-login.component.css']
})
export class ChangePasswordAfterLoginComponent implements OnInit{
  email!: string;
  currentPassword!: string;
  newPassword!: string;
  usuarioActual: any;


  constructor(private route: Router, private loginService: LoginService, private userService: UserService) { }

  ngOnInit(): void {
    this.loginService.getCurrentUser().subscribe(data => {
      this.usuarioActual = data;
    });
  }

  changePassword(){
    this.loginService.changePassword(this.usuarioActual.email, this.currentPassword, this.newPassword).subscribe(
      data =>{
        console.log('Contraseña modificada correctamente');
        this.route.navigate(['/login']);
      },
      error => console.log(error)
    );
  }

}
