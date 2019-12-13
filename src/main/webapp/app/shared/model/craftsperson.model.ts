import { ICraftsperson } from 'app/shared/model/craftsperson.model';

export interface ICraftsperson {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  mentees?: ICraftsperson[];
  mentor?: ICraftsperson;
}

export const defaultValue: Readonly<ICraftsperson> = {};
