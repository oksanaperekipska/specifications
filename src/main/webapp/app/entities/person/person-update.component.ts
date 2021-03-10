import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPerson, Person } from 'app/shared/model/person.model';
import { PersonService } from './person.service';
import { IPersonStatus } from 'app/shared/model/person-status.model';
import { PersonStatusService } from 'app/entities/person-status/person-status.service';

@Component({
  selector: 'jhi-person-update',
  templateUrl: './person-update.component.html',
})
export class PersonUpdateComponent implements OnInit {
  isSaving = false;
  personstatuses: IPersonStatus[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    username: [],
    phone: [],
    lastActiveAt: [null, [Validators.required]],
    statusId: [],
  });

  constructor(
    protected personService: PersonService,
    protected personStatusService: PersonStatusService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ person }) => {
      if (!person.id) {
        const today = moment().startOf('day');
        person.lastActiveAt = today;
      }

      this.updateForm(person);

      this.personStatusService.query().subscribe((res: HttpResponse<IPersonStatus[]>) => (this.personstatuses = res.body || []));
    });
  }

  updateForm(person: IPerson): void {
    this.editForm.patchValue({
      id: person.id,
      name: person.name,
      username: person.username,
      phone: person.phone,
      lastActiveAt: person.lastActiveAt ? person.lastActiveAt.format(DATE_TIME_FORMAT) : null,
      statusId: person.statusId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const person = this.createFromForm();
    if (person.id !== undefined) {
      this.subscribeToSaveResponse(this.personService.update(person));
    } else {
      this.subscribeToSaveResponse(this.personService.create(person));
    }
  }

  private createFromForm(): IPerson {
    return {
      ...new Person(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      username: this.editForm.get(['username'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      lastActiveAt: this.editForm.get(['lastActiveAt'])!.value
        ? moment(this.editForm.get(['lastActiveAt'])!.value, DATE_TIME_FORMAT)
        : undefined,
      statusId: this.editForm.get(['statusId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerson>>): void {
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

  trackById(index: number, item: IPersonStatus): any {
    return item.id;
  }
}
