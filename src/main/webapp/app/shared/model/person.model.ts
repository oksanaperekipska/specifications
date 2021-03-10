import { Moment } from 'moment';
import { IGroup } from 'app/shared/model/group.model';

export interface IPerson {
  id?: number;
  name?: string;
  username?: string;
  phone?: string;
  lastActiveAt?: Moment;
  statusId?: number;
  groups?: IGroup[];
}

export class Person implements IPerson {
  constructor(
    public id?: number,
    public name?: string,
    public username?: string,
    public phone?: string,
    public lastActiveAt?: Moment,
    public statusId?: number,
    public groups?: IGroup[]
  ) {}
}
