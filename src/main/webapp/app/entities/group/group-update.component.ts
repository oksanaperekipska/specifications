import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IGroup, Group } from 'app/shared/model/group.model';
import { GroupService } from './group.service';
import { IPerson } from 'app/shared/model/person.model';
import { PersonService } from 'app/entities/person/person.service';

@Component({
  selector: 'jhi-group-update',
  templateUrl: './group-update.component.html',
})
export class GroupUpdateComponent implements OnInit {
  isSaving = false;
  people: IPerson[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    type: [null, [Validators.required]],
    notification: [],
    superAdminId: [],
    members: [],
  });

  constructor(
    protected groupService: GroupService,
    protected personService: PersonService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ group }) => {
      this.updateForm(group);

      this.personService.query().subscribe((res: HttpResponse<IPerson[]>) => (this.people = res.body || []));
    });
  }

  updateForm(group: IGroup): void {
    this.editForm.patchValue({
      id: group.id,
      name: group.name,
      type: group.type,
      notification: group.notification,
      superAdminId: group.superAdminId,
      members: group.members,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const group = this.createFromForm();
    if (group.id !== undefined) {
      this.subscribeToSaveResponse(this.groupService.update(group));
    } else {
      this.subscribeToSaveResponse(this.groupService.create(group));
    }
  }

  private createFromForm(): IGroup {
    return {
      ...new Group(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      type: this.editForm.get(['type'])!.value,
      notification: this.editForm.get(['notification'])!.value,
      superAdminId: this.editForm.get(['superAdminId'])!.value,
      members: this.editForm.get(['members'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGroup>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IPerson): any {
    return item.id;
  }

  getSelected(selectedVals: IPerson[], option: IPerson): IPerson {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
