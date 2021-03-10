import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPersonStatus, PersonStatus } from 'app/shared/model/person-status.model';
import { PersonStatusService } from './person-status.service';
import { PersonStatusComponent } from './person-status.component';
import { PersonStatusDetailComponent } from './person-status-detail.component';
import { PersonStatusUpdateComponent } from './person-status-update.component';

@Injectable({ providedIn: 'root' })
export class PersonStatusResolve implements Resolve<IPersonStatus> {
  constructor(private service: PersonStatusService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPersonStatus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((personStatus: HttpResponse<PersonStatus>) => {
          if (personStatus.body) {
            return of(personStatus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PersonStatus());
  }
}

export const personStatusRoute: Routes = [
  {
    path: '',
    component: PersonStatusComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PersonStatuses',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonStatusDetailComponent,
    resolve: {
      personStatus: PersonStatusResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PersonStatuses',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonStatusUpdateComponent,
    resolve: {
      personStatus: PersonStatusResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PersonStatuses',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonStatusUpdateComponent,
    resolve: {
      personStatus: PersonStatusResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PersonStatuses',
    },
    canActivate: [UserRouteAccessService],
  },
];
