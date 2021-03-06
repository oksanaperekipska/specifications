import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITag, Tag } from 'app/shared/model/tag.model';
import { TagService } from './tag.service';
import { IGroup } from 'app/shared/model/group.model';
import { GroupService } from 'app/entities/group/group.service';

@Component({
  selector: 'jhi-tag-update',
  templateUrl: './tag-update.component.html',
})
export class TagUpdateComponent implements OnInit {
  isSaving = false;
  groups: IGroup[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    groupId: [],
  });

  constructor(
    protected tagService: TagService,
    protected groupService: GroupService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tag }) => {
      this.updateForm(tag);

      this.groupService.query().subscribe((res: HttpResponse<IGroup[]>) => (this.groups = res.body || []));
    });
  }

  updateForm(tag: ITag): void {
    this.editForm.patchValue({
      id: tag.id,
      name: tag.name,
      groupId: tag.groupId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tag = this.createFromForm();
    if (tag.id !== undefined) {
      this.subscribeToSaveResponse(this.tagService.update(tag));
    } else {
      this.subscribeToSaveResponse(this.tagService.create(tag));
    }
  }

  private createFromForm(): ITag {
    return {
      ...new Tag(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      groupId: this.editForm.get(['groupId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITag>>): void {
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

  trackById(index: number, item: IGroup): any {
    return item.id;
  }
}
