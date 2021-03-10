import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'person',
        loadChildren: () => import('./person/person.module').then(m => m.SpecificationsPersonModule),
      },
      {
        path: 'group',
        loadChildren: () => import('./group/group.module').then(m => m.SpecificationsGroupModule),
      },
      {
        path: 'tag',
        loadChildren: () => import('./tag/tag.module').then(m => m.SpecificationsTagModule),
      },
      {
        path: 'person-status',
        loadChildren: () => import('./person-status/person-status.module').then(m => m.SpecificationsPersonStatusModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class SpecificationsEntityModule {}
