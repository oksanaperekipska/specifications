import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPersonStatus } from 'app/shared/model/person-status.model';
import { PersonStatusService } from './person-status.service';

@Component({
  templateUrl: './person-status-delete-dialog.component.html',
})
export class PersonStatusDeleteDialogComponent {
  personStatus?: IPersonStatus;

  constructor(
    protected personStatusService: PersonStatusService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.personStatusService.delete(id).subscribe(() => {
      this.eventManager.broadcast('personStatusListModification');
      this.activeModal.close();
    });
  }
}
