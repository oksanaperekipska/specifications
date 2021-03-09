import { NgModule } from '@angular/core';
import { SpecificationsSharedLibsModule } from './shared-libs.module';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';

@NgModule({
  imports: [SpecificationsSharedLibsModule],
  declarations: [AlertComponent, AlertErrorComponent, HasAnyAuthorityDirective],
  exports: [SpecificationsSharedLibsModule, AlertComponent, AlertErrorComponent, HasAnyAuthorityDirective],
})
export class SpecificationsSharedModule {}
