import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGroup } from 'app/shared/model/group.model';
import { GroupService } from './group.service';
import { GroupDeleteDialogComponent } from './group-delete-dialog.component';

@Component({
  selector: 'jhi-group',
  templateUrl: './group.component.html',
})
export class GroupComponent implements OnInit, OnDestroy {
  groups?: IGroup[];
  eventSubscriber?: Subscription;

  constructor(protected groupService: GroupService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.groupService.query().subscribe((res: HttpResponse<IGroup[]>) => (this.groups = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInGroups();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IGroup): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInGroups(): void {
    this.eventSubscriber = this.eventManager.subscribe('groupListModification', () => this.loadAll());
  }

  delete(group: IGroup): void {
    const modalRef = this.modalService.open(GroupDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.group = group;
  }
}
