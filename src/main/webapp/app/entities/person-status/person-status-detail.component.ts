import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonStatus } from 'app/shared/model/person-status.model';

@Component({
  selector: 'jhi-person-status-detail',
  templateUrl: './person-status-detail.component.html',
})
export class PersonStatusDetailComponent implements OnInit {
  personStatus: IPersonStatus | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personStatus }) => (this.personStatus = personStatus));
  }

  previousState(): void {
    window.history.back();
  }
}
