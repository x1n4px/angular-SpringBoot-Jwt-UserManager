import { NormalGuard } from './services/normal.guard';
import { AdminGuard } from './services/admin.guard';
import { UserDashboardComponent } from './pages/user/user-dashboard/user-dashboard.component';
import { DashboardComponent } from './pages/admin/dashboard/dashboard.component';
import { LoginComponent } from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';
import { HomeComponent } from './pages/home/home.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProfileComponent } from './pages/profile/profile.component';
import { ChangePasswordAfterLoginComponent } from './pages/change-password-after-login/change-password-after-login.component';
import { ChangePassNoLoginComponent } from './pages/change-pass-no-login/change-pass-no-login.component';
import { EditUserComponent } from './pages/admin/edit-user/edit-user.component';

const routes: Routes = [
  {
    path : '',
    component : HomeComponent,
    pathMatch : 'full'
  },
  {
    path : 'signup',
    component : SignupComponent,
    pathMatch : 'full'
  },
  {
    path : 'login',
    component : LoginComponent,
    pathMatch : 'full'
  },
  {
    path : 'change-password-after-login',
    component : ChangePasswordAfterLoginComponent,
    pathMatch : 'full'
  },
 {
  path : 'change-password-no-login',
  component : ChangePassNoLoginComponent,
  pathMatch : 'full'
 } ,
  {
    path:'admin',
    component:DashboardComponent,
    pathMatch:'full',
  },
  {
    path:'user-dashboard',
    component:UserDashboardComponent,
    pathMatch:'full',
    canActivate:[NormalGuard]
  },
  {
    path : 'profile',
    component : ProfileComponent,
    pathMatch : 'full',
  },
  {
    path:'editUser/:id',
    component:EditUserComponent,
    pathMatch:'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
