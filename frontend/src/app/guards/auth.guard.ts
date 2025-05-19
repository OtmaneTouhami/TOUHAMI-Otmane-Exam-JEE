import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    if (this.authService.isAuthenticated()) {
      const requiredRoles = route.data['roles'] as Array<string>;
      if (requiredRoles && requiredRoles.length > 0) {
        const userRoles = this.authService.getRoles();
        const hasRequiredRole = requiredRoles.some(role => userRoles.includes(role));
        if (hasRequiredRole) {
          return true;
        } else {
          // Not authorized for this specific role
          // You might want to redirect to an 'unauthorized' page or back to login
          this.router.navigate(['/login']); // Or an unauthorized page
          return false;
        }
      }
      return true; // Authenticated and no specific roles required, or roles check passed
    } else {
      this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
      return false;
    }
  }
} 