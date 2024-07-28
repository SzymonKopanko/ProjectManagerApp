import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Project } from '../models/project.model';
import { Task } from '../models/task.model';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private apiUrl = 'http://localhost:8081/api/projects';

  constructor(private http: HttpClient) { }

  getProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(this.apiUrl);
  }

  getProjectById(id: number): Observable<Project> {
    return this.http.get<Project>(`${this.apiUrl}/${id}`);
  }

  createProject(title: string): Observable<Project> {
    return this.http.post<Project>(this.apiUrl, { title });
  }

  updateProject(project: Project): Observable<Project> {
    return this.http.put<Project>(`${this.apiUrl}/${project.id}`, project);
  }

  deleteProject(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  addTaskToProject(projectId: number, task: Task): Observable<Task> {
    return this.http.post<Task>(`${this.apiUrl}/${projectId}/tasks`, task);
  }

  updateTaskInProject(projectId: number, taskId: number, task: Task): Observable<Task> {
    return this.http.put<Task>(`${this.apiUrl}/${projectId}/tasks/${taskId}`, task);
  }

  deleteTaskFromProject(projectId: number, taskId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${projectId}/tasks/${taskId}`);
  }

  assignSelfToTask(projectId: number, taskId: number): Observable<Task> {
    return this.http.put<Task>(`${this.apiUrl}/${projectId}/tasks/${taskId}/assign`, {});
  }

  unassignSelfFromTask(projectId: number, taskId: number): Observable<Task> {
    return this.http.put<Task>(`${this.apiUrl}/${projectId}/tasks/${taskId}/unassign`, {});
  }

  addUserToProject(projectId: number, userId: number): Observable<Project> {
    return this.http.put<Project>(`${this.apiUrl}/${projectId}/users/${userId}`, {});
  }
}
