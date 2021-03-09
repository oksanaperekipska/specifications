import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IGroup, Group } from 'app/shared/model/group.model';
import { GroupService } from './group.service';
import { GroupComponent } from './group.component';
import { GroupDetailComponent } from './group-detail.component';
import { GroupUpdateComponent } from './group-update.component';

@Injectable({ providedIn: 'root' })
export class GroupResolve implements Resolve<IGroup> {
  constructor(private service: GroupService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGroup> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((group: HttpResponse<Group>) => {
          if (group.body) {
            return of(group.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Group());
  }
}

export const groupRoute: Routes = [
  {
    path: '',
    component: GroupComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Groups',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GroupDetailComponent,
    resolve: {
      group: GroupResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Groups',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GroupUpdateComponent,
    resolve: {
      group: GroupResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Groups',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GroupUpdateComponent,
    resolve: {
      group: GroupResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Groups',
    },
    canActivate: [UserRouteAccessService],
  },
];
