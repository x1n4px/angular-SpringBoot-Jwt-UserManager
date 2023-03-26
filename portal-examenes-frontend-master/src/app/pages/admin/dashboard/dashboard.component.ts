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
  confirmacionEliminacion = false;
  mostrarTablaUsuarios: boolean = false;
  mostrarTablaVigiantes: boolean = false;
  mostrarTablaAdmin: boolean = false;
  mostrarTablaRAULA:boolean = false;
  mostrarTablaPServicio:boolean = false;
  mostrarTablaRSEDES:boolean = false;
  mostrarTablaVocal: boolean = false;
  usuarioActual: any;
  terminoBusqueda!: string;
  resultados!: any[];



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
    this.loginService.getCurrentUser().subscribe(data => {
      this.usuarioActual = data;
    });
  }

  buscar() {
    if (this.terminoBusqueda && this.terminoBusqueda.trim()) {
      this.resultados = this.usuarios.filter((usuario) => {
        const termino = this.terminoBusqueda.toLowerCase();
        return usuario.nombre.toLowerCase().includes(termino) || usuario.apellido.toLowerCase().includes(termino) || usuario.rolAsignado.toLowerCase().includes(termino) ;
      });
    } else {
      this.resultados = [];
    }
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


  activarConfirmacionEliminacion() {
    this.confirmacionEliminacion = true;
  }

  alternarTablaUsuario(): void {
    this.mostrarTablaUsuarios = !this.mostrarTablaUsuarios;
  }

  alternarTablaVigilantes():void{
    this.mostrarTablaVigiantes = !this.mostrarTablaVigiantes;
  }

  mostrarTablaAdministradores():void{
    this.mostrarTablaAdmin = !this.mostrarTablaAdmin;
  }

  alternarTablaVocal():void{
    this.mostrarTablaVocal = !this.mostrarTablaVocal;
  }

  alternarTablaRSEDES():void{
    this.mostrarTablaRSEDES = !this.mostrarTablaRSEDES;
  }
  alternarTablaRAULA():void{
    this.mostrarTablaRAULA = !this.mostrarTablaRAULA;
  }
  alternarTablaPServicio():void{
    this.mostrarTablaPServicio = !this.mostrarTablaPServicio;
  }
}
