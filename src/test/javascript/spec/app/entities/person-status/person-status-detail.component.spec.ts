import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpecificationsTestModule } from '../../../test.module';
import { PersonStatusDetailComponent } from 'app/entities/person-status/person-status-detail.component';
import { PersonStatus } from 'app/shared/model/person-status.model';

describe('Component Tests', () => {
  describe('PersonStatus Management Detail Component', () => {
    let comp: PersonStatusDetailComponent;
    let fixture: ComponentFixture<PersonStatusDetailComponent>;
    const route = ({ data: of({ personStatus: new PersonStatus(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpecificationsTestModule],
        declarations: [PersonStatusDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PersonStatusDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PersonStatusDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load personStatus on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.personStatus).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
