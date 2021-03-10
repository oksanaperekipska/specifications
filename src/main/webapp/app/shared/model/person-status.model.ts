import { PStatus } from 'app/shared/model/enumerations/p-status.model';

export interface IPersonStatus {
  id?: number;
  code?: PStatus;
  title?: string;
  description?: string;
}

export class PersonStatus implements IPersonStatus {
  constructor(public id?: number, public code?: PStatus, public title?: string, public description?: string) {}
}
