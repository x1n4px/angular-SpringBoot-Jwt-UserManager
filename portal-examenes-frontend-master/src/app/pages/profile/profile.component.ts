import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { LoginService } from './../../services/login.service';


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  usuarioActual: any;
  constructor(private userService: UserService, private loginService:LoginService) { }

  ngOnInit() {
    this.loginService.getCurrentUser().subscribe(data => {
      this.usuarioActual = data;
    });
  }


}
