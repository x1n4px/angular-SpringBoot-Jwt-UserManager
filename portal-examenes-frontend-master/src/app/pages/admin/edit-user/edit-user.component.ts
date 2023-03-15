import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import { User } from 'src/app/User';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent implements OnInit {
  id!:number;
  usuario!:User;
  constructor(private route:ActivatedRoute, private loginService: LoginService, private router:Router) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];
    this.usuario = new User();
    this.loginService.obtenerUsuarioPorId(this.id).subscribe(dato => {
      this.usuario = dato;
    })
  }

  guardarUsuario(){
    console.log(this.id);
    console.log(this.usuario);
    this.loginService.actualizarUsuario(this.id, this.usuario).subscribe(dato => {
      this.router.navigate(['admin']);
    })
  }

  volver(){
    this.router.navigate(['admin']);
  }
}
