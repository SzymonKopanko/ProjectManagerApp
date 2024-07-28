import {User} from "./user.model";
import {Task} from "./task.model";

export interface Project {
  id?: number; // `id` jest opcjonalne, ponieważ nowy projekt może nie mieć jeszcze przypisanego identyfikatora
  title: string;
  users?: User[]; // Zależnie od implementacji, użytkownicy mogą być opcjonalni
  tasks?: Task[]; // Zależnie od implementacji, zadania mogą być opcjonalne
}
