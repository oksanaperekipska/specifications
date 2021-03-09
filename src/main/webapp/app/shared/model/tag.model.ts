export interface ITag {
  id?: number;
  name?: string;
  groupId?: number;
}

export class Tag implements ITag {
  constructor(public id?: number, public name?: string, public groupId?: number) {}
}
