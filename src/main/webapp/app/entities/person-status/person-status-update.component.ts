import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPersonStatus, PersonStatus } from 'app/shared/model/person-status.model';
import { PersonStatusService } from './person-status.service';

@Component({
  selector: 'jhi-person-status-update',
  templateUrl: './person-status-update.component.html',
})
export class PersonStatusUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    code: [null, [Validators.required]],
    title: [null, [Validators.required]],
    description: [],
  });

  constructor(protected personStatusService: PersonStatusService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personStatus }) => {
      this.updateForm(personStatus);
    });
  }

  updateForm(personStatus: IPersonStatus): void {
    this.editForm.patchValue({
      id: personStatus.id,
      code: personStatus.code,
      title: personStatus.title,
      description: personStatus.description,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personStatus = this.createFromForm();
    if (personStatus.id !== undefined) {
      this.subscribeToSaveResponse(this.personStatusService.update(personStatus));
    } else {
      this.subscribeToSaveResponse(this.personStatusService.create(personStatus));
    }
  }

  private createFromForm(): IPersonStatus {
    return {
      ...new PersonStatus(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonStatus>>): void {
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
}
