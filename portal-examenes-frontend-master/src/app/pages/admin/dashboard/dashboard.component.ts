import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import { User } from 'src/app/User';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  usuarios: User[] = [];

  constructor(private loginService: LoginService, private route: Router) { }

  ngOnInit(): void {
    this.loginService.getAllUser().subscribe(
      (usuarios: User[]) => {
        this.usuarios = usuarios;
      },
      (error) => {
        console.log(error);
      }
    );
  }

  eliminarUsuario(id:number){
    this.loginService.eliminarUsuario(id).subscribe(dato =>{
      console.log(dato);
      this.obtenerUsuarios();
    })
  }

  private obtenerUsuarios(){
    this.loginService.getAllUser().subscribe(
      (usuarios: User[]) => {
        this.usuarios = usuarios;
      },
      (error) => {
        console.log(error);
      }
    );
  }

  verDetallesUsuario(id:number){
    this.route.navigate(['editUser', id])
  }


  vistaUsuario(){
    this.route.navigate(['user-dashboard']);
  }
}
