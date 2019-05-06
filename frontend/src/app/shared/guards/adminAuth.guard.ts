import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AdminAuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}
  
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (this.authService.isAuthenticated() && this.authService.getRole() === "ADMIN") {
      return true;
    } else {
      this.router.navigate(['auth/signin'], {
        queryParams: {
          return: state.url
        }
      });
      return false;
    }
  }
}