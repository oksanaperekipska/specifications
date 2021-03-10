import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SpecificationsTestModule } from '../../../test.module';
import { PersonStatusComponent } from 'app/entities/person-status/person-status.component';
import { PersonStatusService } from 'app/entities/person-status/person-status.service';
import { PersonStatus } from 'app/shared/model/person-status.model';

describe('Component Tests', () => {
  describe('PersonStatus Management Component', () => {
    let comp: PersonStatusComponent;
    let fixture: ComponentFixture<PersonStatusComponent>;
    let service: PersonStatusService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpecificationsTestModule],
        declarations: [PersonStatusComponent],
      })
        .overrideTemplate(PersonStatusComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonStatusComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PersonStatusService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new PersonStatus(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.personStatuses && comp.personStatuses[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
