import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SpecificationsTestModule } from '../../../test.module';
import { PersonStatusUpdateComponent } from 'app/entities/person-status/person-status-update.component';
import { PersonStatusService } from 'app/entities/person-status/person-status.service';
import { PersonStatus } from 'app/shared/model/person-status.model';

describe('Component Tests', () => {
  describe('PersonStatus Management Update Component', () => {
    let comp: PersonStatusUpdateComponent;
    let fixture: ComponentFixture<PersonStatusUpdateComponent>;
    let service: PersonStatusService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpecificationsTestModule],
        declarations: [PersonStatusUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PersonStatusUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonStatusUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PersonStatusService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PersonStatus(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new PersonStatus();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
