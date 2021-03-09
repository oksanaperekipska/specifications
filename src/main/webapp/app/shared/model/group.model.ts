import { ITag } from 'app/shared/model/tag.model';
import { IPerson } from 'app/shared/model/person.model';
import { GroupType } from 'app/shared/model/enumerations/group-type.model';

export interface IGroup {
  id?: number;
  name?: string;
  type?: GroupType;
  notification?: boolean;
  tags?: ITag[];
  superAdminId?: number;
  members?: IPerson[];
}

export class Group implements IGroup {
  constructor(
    public id?: number,
    public name?: string,
    public type?: GroupType,
    public notification?: boolean,
    public tags?: ITag[],
    public superAdminId?: number,
    public members?: IPerson[]
  ) {
    this.notification = this.notification || false;
  }
}
