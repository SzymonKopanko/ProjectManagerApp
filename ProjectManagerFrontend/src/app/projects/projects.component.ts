import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../services/project.service';
import { Project } from '../models/project.model';
import { Task } from '../models/task.model';

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.css']
})
export class ProjectsComponent implements OnInit {
  projects: Project[] = [];
  newProjectTitle: string = '';
  newTaskTitle: string = '';
  newTaskDescription: string = '';

  constructor(private projectService: ProjectService) {}

  ngOnInit(): void {
    this.loadProjects();
  }

  loadProjects(): void {
    this.projectService.getProjects().subscribe((projects) => {
      this.projects = projects;
    });
  }

  createProject(): void {
    this.projectService.createProject(this.newProjectTitle).subscribe(() => {
      this.loadProjects();
      this.newProjectTitle = '';
    });
  }

  addTask(projectId: number): void {
    const newTask: Task = {
      title: this.newTaskTitle,
      description: this.newTaskDescription,
      completed: false,
      projectId: projectId
    };
    this.projectService.addTaskToProject(projectId, newTask).subscribe(() => {
      this.loadProjects();
      this.newTaskTitle = '';
      this.newTaskDescription = '';
    });
  }
}
