import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-user-dashboard',
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['./user-dashboard.component.css']
})
export class UserDashboardComponent implements OnInit {
  usuarioActual: any;
  rolAsignado!: string;
  constructor(private route: Router, private loginService:LoginService) { }

  ngOnInit(): void {
    this.loginService.getCurrentUser().subscribe(data => {
      this.usuarioActual = data;
    });
    this.rolAsignado = this.usuarioActual.rolAsignado;
  }




  funcionParaAdmin(){
    this.route.navigate(['admin']);

  }

}
