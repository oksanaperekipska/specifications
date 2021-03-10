import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpecificationsSharedModule } from 'app/shared/shared.module';
import { PersonStatusComponent } from './person-status.component';
import { PersonStatusDetailComponent } from './person-status-detail.component';
import { PersonStatusUpdateComponent } from './person-status-update.component';
import { PersonStatusDeleteDialogComponent } from './person-status-delete-dialog.component';
import { personStatusRoute } from './person-status.route';

@NgModule({
  imports: [SpecificationsSharedModule, RouterModule.forChild(personStatusRoute)],
  declarations: [PersonStatusComponent, PersonStatusDetailComponent, PersonStatusUpdateComponent, PersonStatusDeleteDialogComponent],
  entryComponents: [PersonStatusDeleteDialogComponent],
})
export class SpecificationsPersonStatusModule {}
