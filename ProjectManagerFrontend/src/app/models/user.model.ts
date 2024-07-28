export interface User {
  id?: number; // `id` jest opcjonalne, ponieważ nowy użytkownik może nie mieć jeszcze przypisanego identyfikatora
  username: string;
  email: string;
}
