import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import { UserService } from 'src/app/services/user.service';
import { HttpClient } from '@angular/common/http';
import { User } from 'src/app/User';

@Component({
  selector: 'app-change-pass-no-login',
  templateUrl: './change-pass-no-login.component.html',
  styleUrls: ['./change-pass-no-login.component.css']
})
export class ChangePassNoLoginComponent implements OnInit {
  email!:string;
  constructor(private route: Router, private loginService: UserService) { }

  ngOnInit(): void {
  }

  requestNewPassword(){
    this.loginService.requestNewPassword(this.email).subscribe(
      data => {
        console.log('Solicitada la nueva clave');
      },
      (error) => console.log(error)
      );
    }
  }

