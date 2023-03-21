import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import baserUrl from './helper';
import { map } from 'rxjs';
import { User } from '../User';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private URL = "http://localhost:8080";
  private eURL = "http://localhost:8080/eliminarUsuario";
  private rURL = "http://localhost:8080/recover-password";
  private rqURL = "http://localhost:8080/requestNewPassword";
  private gUURL = "http://localhost:8080/user";



  public loginStatusSubjec = new Subject<boolean>();

  constructor(private http:HttpClient) { }

  //generamos el token
  public generateToken(loginData:any){
    return this.http.post(`${baserUrl}/generate-token`,loginData);
  }

  public getCurrentUser(){
    return this.http.get(`${baserUrl}/user/actual`);
  }

  public getAllUser(){
    return this.http.get<User[]>(`${baserUrl}/user/todos`);
  }

  eliminarUsuario(id:number): Observable<Object>{
    return this.http.delete(`${baserUrl}/user/eliminarUsuario/${id}`);  }


  changePassword(email: string, currentPassword: string, newPassword: string): Observable<any>{
    const body = {email, currentPassword, newPassword};
    return this.http.post<any>("http://localhost:8080/user/perfil/cambiarClave", body);
  }

  obtenerUsuarioPorId(id:number):Observable<User>{
    return this.http.get<User>(`${this.gUURL}/${id}`);
  }

  actualizarUsuario(id:number, usuario:User) : Observable<Object>{
    return this.http.put(`${this.gUURL}/${id}`, usuario);
  }




  //iniciamos sesión y establecemos el token en el localStorage
  public loginUser(token:any){
    localStorage.setItem('token',token);
    return true;
  }

  public isLoggedIn(){
    let tokenStr = localStorage.getItem('token');
    if(tokenStr == undefined || tokenStr == '' || tokenStr == null){
      return false;
    }else{
      return true;
    }
  }

  //cerranis sesion y eliminamos el token del localStorage
  public logout(){
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    return true;
  }

  //obtenemos el token
  public getToken(){
    return localStorage.getItem('token');
  }

  public setUser(user:any){
    localStorage.setItem('user', JSON.stringify(user));
  }

  public getUser(){
    let userStr = localStorage.getItem('user');
    if(userStr != null){
      return JSON.parse(userStr);
    }else{
      this.logout();
      return null;
    }
  }

  public getUserRole(){
    let user = this.getUser();
    return user.rolAsignado;
  }

  getUserDetails() {
    return this.http.get("http://localhost:8080/profile");
  }

}
