import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SpecificationsTestModule } from '../../../test.module';
import { GroupComponent } from 'app/entities/group/group.component';
import { GroupService } from 'app/entities/group/group.service';
import { Group } from 'app/shared/model/group.model';

describe('Component Tests', () => {
  describe('Group Management Component', () => {
    let comp: GroupComponent;
    let fixture: ComponentFixture<GroupComponent>;
    let service: GroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpecificationsTestModule],
        declarations: [GroupComponent],
      })
        .overrideTemplate(GroupComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GroupComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GroupService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Group(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.groups && comp.groups[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
