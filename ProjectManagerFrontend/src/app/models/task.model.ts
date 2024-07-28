import {User} from "./user.model";

export interface Task {
  id?: number; // `id` jest opcjonalne, ponieważ nowe zadanie może nie mieć jeszcze przypisanego identyfikatora
  title: string;
  description: string;
  completed: boolean;
  projectId: number; // `projectId` jest opcjonalne, ponieważ nowe zadanie może nie mieć jeszcze przypisanego identyfikatora projektu
  users?: User[]; // Zależnie od implementacji, użytkownicy mogą być opcjonalni
}
