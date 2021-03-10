import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonStatus } from 'app/shared/model/person-status.model';
import { PersonStatusService } from './person-status.service';
import { PersonStatusDeleteDialogComponent } from './person-status-delete-dialog.component';

@Component({
  selector: 'jhi-person-status',
  templateUrl: './person-status.component.html',
})
export class PersonStatusComponent implements OnInit, OnDestroy {
  personStatuses?: IPersonStatus[];
  eventSubscriber?: Subscription;

  constructor(
    protected personStatusService: PersonStatusService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.personStatusService.query().subscribe((res: HttpResponse<IPersonStatus[]>) => (this.personStatuses = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPersonStatuses();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPersonStatus): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPersonStatuses(): void {
    this.eventSubscriber = this.eventManager.subscribe('personStatusListModification', () => this.loadAll());
  }

  delete(personStatus: IPersonStatus): void {
    const modalRef = this.modalService.open(PersonStatusDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.personStatus = personStatus;
  }
}
