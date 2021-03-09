import { Moment } from 'moment';
import { IGroup } from 'app/shared/model/group.model';
import { PersonStatus } from 'app/shared/model/enumerations/person-status.model';

export interface IPerson {
  id?: number;
  name?: string;
  username?: string;
  phone?: string;
  status?: PersonStatus;
  lastActiveAt?: Moment;
  groups?: IGroup[];
}

export class Person implements IPerson {
  constructor(
    public id?: number,
    public name?: string,
    public username?: string,
    public phone?: string,
    public status?: PersonStatus,
    public lastActiveAt?: Moment,
    public groups?: IGroup[]
  ) {}
}
