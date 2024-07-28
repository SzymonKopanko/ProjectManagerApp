package skopanko.projectmanagerapi.controller;

//import org.slf4j.logger;
//import org.slf4j.loggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import skopanko.projectmanagerapi.dto.ProjectDTO;
import skopanko.projectmanagerapi.dto.TaskDTO;
import skopanko.projectmanagerapi.entity.AppUser;
import skopanko.projectmanagerapi.entity.Project;
import skopanko.projectmanagerapi.entity.Task;
import skopanko.projectmanagerapi.mapper.ProjectMapper;
import skopanko.projectmanagerapi.mapper.TaskMapper;
import skopanko.projectmanagerapi.service.ProjectService;
import skopanko.projectmanagerapi.service.TaskService;
import skopanko.projectmanagerapi.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    //private static final Logger logger = loggerFactory.getlogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<ProjectDTO> getAllProjects(Authentication authentication) {
        Long userId = userService.findUserByUsername(authentication.getName()).getId();
        return projectService.findAllProjectsByUserId(userId).stream()
                .map(ProjectMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id, Authentication authentication) {
        Long userId = userService.findUserByUsername(authentication.getName()).getId();
        Optional<Project> project = projectService.findProjectById(id);
        if (project.isPresent()) {
            Project projectEntity = project.get();
            //projectEntity.setUsers(userService.findAppUsersByProjectId(id));
            //projectEntity.setTasks(taskService.findTasksByProjectId(id));
            //logger.info("Project ID: {}, Users: {}", projectEntity.getId(), projectEntity.getUsers());
            boolean userHasAccess = projectEntity.getUsers().stream().anyMatch(user -> user.getId().equals(userId));
            if (userHasAccess) {
                return ResponseEntity.ok(ProjectMapper.toDto(projectEntity));
            } else {
                List<AppUser> usersWithAccess = userService.findAppUsersByProjectId(id);
                //logger.warn("User ID {} does not have access to project ID {}. Users with access: {}", userId, id, usersWithAccess.stream().map(AppUser::getUsername).collect(Collectors.toList()));
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        return ResponseEntity.notFound().build();
    }




    @PostMapping
    public ProjectDTO createProject(@RequestBody ProjectDTO projectDTO, Authentication authentication) {
        String username = authentication.getName();
        //logger.info("Creating project for user: {}", username);

        try {
            Long userId = userService.findUserByUsername(username).getId();
            Project project = ProjectMapper.toEntity(projectDTO);

            if (project.getUsers() == null) {
                AppUser user = userService.findUserById(userId).orElseThrow(() -> new RuntimeException("User not found"));
                List<AppUser> users = new ArrayList<>();
                users.add(user);
                project.setUsers(users);
            }


            Project savedProject = projectService.saveProject(project);
            //logger.info("Project created with ID: {}", savedProject.getId());

            return ProjectMapper.toDto(savedProject);
        } catch (Exception e) {
            //logger.error("Error creating project: ", e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO, Authentication authentication) {
        Long userId = userService.findUserByUsername(authentication.getName()).getId();
        Optional<Project> project = projectService.findProjectById(id);

        if (project.isPresent()) {
            Project projectEntity = project.get();
            //projectEntity.setUsers(userService.findAppUsersByProjectId(id));
            //projectEntity.setTasks(taskService.findTasksByProjectId(id));
            //logger.info("Project ID: {}, Users: {}", projectEntity.getId(), projectEntity.getUsers());

            boolean userHasAccess = projectEntity.getUsers().stream().anyMatch(user -> user.getId().equals(userId));
            if (userHasAccess) {
                Project projectToUpdate = ProjectMapper.toEntity(projectDTO);
                projectToUpdate.setId(projectEntity.getId());
                Project updatedProject = projectService.saveProject(projectToUpdate);
                return ResponseEntity.ok(ProjectMapper.toDto(updatedProject));
            } else {
                List<AppUser> usersWithAccess = userService.findAppUsersByProjectId(id);
                //logger.warn("User ID {} does not have access to project ID {}. Users with access: {}", userId, id, usersWithAccess.stream().map(AppUser::getUsername).collect(Collectors.toList()));
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id, Authentication authentication) {
        Long userId = userService.findUserByUsername(authentication.getName()).getId();
        Optional<Project> project = projectService.findProjectById(id);

        if (project.isPresent()) {
            Project projectEntity = project.get();
            //projectEntity.setUsers(userService.findAppUsersByProjectId(id));
            //logger.info("Completed setting users for project");
            //projectEntity.setTasks(taskService.findTasksByProjectId(id));

            boolean userHasAccess = projectEntity.getUsers().stream().anyMatch(user -> user.getId().equals(userId));
            if (userHasAccess) {
                projectService.deleteProject(id);
                return ResponseEntity.ok().build();
            } else {
                List<AppUser> usersWithAccess = userService.findAppUsersByProjectId(id);
                //logger.warn("User ID {} does not have access to project ID {}. Users with access: {}", userId, id, usersWithAccess.stream().map(AppUser::getUsername).collect(Collectors.toList()));
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<TaskDTO> addTaskToProject(@PathVariable Long projectId, @RequestBody TaskDTO taskDTO, Authentication authentication) {
        try {
            Long userId = userService.findUserByUsername(authentication.getName()).getId();
            Optional<Project> project = projectService.findProjectById(projectId);

            if (project.isPresent()) {
                Project projectEntity = project.get();
                //projectEntity.setUsers(userService.findAppUsersByProjectId(projectId));
                //projectEntity.setTasks(taskService.findTasksByProjectId(projectId));
                //logger.info("Project ID: {}, Users: {}", projectEntity.getId(), projectEntity.getUsers());

                boolean userHasAccess = projectEntity.getUsers().stream().anyMatch(user -> user.getId().equals(userId));
                if (userHasAccess) {
                    Task task = TaskMapper.toEntity(taskDTO);
                    task.setProject(projectEntity);
                    Task savedTask = taskService.saveTask(task);
                    //logger.info("Task added to project with ID {} by user with ID {}.", projectId, userId);
                    return ResponseEntity.ok(TaskMapper.toDto(savedTask));
                } else {
                    String userNames = projectEntity.getUsers().stream().map(AppUser::getUsername).collect(Collectors.joining(", "));
                    //logger.warn("User with ID {} does not have access to project with ID {}. Users with access: {}", userId, projectId, userNames);
                    return ResponseEntity.status(403).build();
                }
            } else {
                //logger.warn("Project with ID {} not found.", projectId);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            //logger.error("Error adding task to project: ", e);
            throw e;
        }
    }

    @PutMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<TaskDTO> updateTaskInProject(@PathVariable Long projectId, @PathVariable Long taskId, @RequestBody TaskDTO taskDTO, Authentication authentication) {
        Long userId = userService.findUserByUsername(authentication.getName()).getId();
        Optional<Project> project = projectService.findProjectById(projectId);

        if (project.isPresent()) {
            Project projectEntity = project.get();
            //projectEntity.setUsers(userService.findAppUsersByProjectId(projectId));
            //projectEntity.setTasks(taskService.findTasksByProjectId(projectId));
            //logger.info("Project ID: {}, Users: {}", projectEntity.getId(), projectEntity.getUsers());

            boolean userHasAccess = projectEntity.getUsers().stream().anyMatch(user -> user.getId().equals(userId));
            if (userHasAccess) {
                Optional<Task> task = taskService.findTaskById(taskId);

                if (task.isPresent()) {
                    Task taskEntity = task.get();
                    taskEntity.setProject(projectEntity);
                    taskEntity.setUsers(userService.findAppUsersByTaskId(taskId));
                    if (taskEntity.getProject().getId().equals(projectId)) {
                        Task taskToUpdate = TaskMapper.toEntity(taskDTO);
                        taskToUpdate.setId(task.get().getId());
                        taskToUpdate.setProject(projectEntity);
                        Task updatedTask = taskService.saveTask(taskToUpdate);
                        return ResponseEntity.ok(TaskMapper.toDto(updatedTask));
                    }
                }

            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<?> deleteTaskFromProject(@PathVariable Long projectId, @PathVariable Long taskId, Authentication authentication) {
        Long userId = userService.findUserByUsername(authentication.getName()).getId();
        Optional<Project> project = projectService.findProjectById(projectId);

        if (project.isPresent()) {
            Project projectEntity = project.get();
            //projectEntity.setUsers(userService.findAppUsersByProjectId(projectId));
            //projectEntity.setTasks(taskService.findTasksByProjectId(projectId));
            //logger.info("Project ID: {}, Users: {}", projectEntity.getId(), projectEntity.getUsers());

            boolean userHasAccess = projectEntity.getUsers().stream().anyMatch(user -> user.getId().equals(userId));
            if (userHasAccess) {
                Optional<Task> task = taskService.findTaskById(taskId);
                if (task.isPresent()) {
                    taskService.deleteTask(taskId);
                    return ResponseEntity.ok().build();
                }
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{projectId}/tasks/{taskId}/assign")
    public ResponseEntity<TaskDTO> assignSelfToTask(@PathVariable Long projectId, @PathVariable Long taskId, Authentication authentication) {
        Long userId = userService.findUserByUsername(authentication.getName()).getId();
        Optional<Project> project = projectService.findProjectById(projectId);

        if (project.isPresent()) {
            Project projectEntity = project.get();
            //projectEntity.setUsers(userService.findAppUsersByProjectId(projectId));
            //projectEntity.setTasks(taskService.findTasksByProjectId(projectId));
            //logger.info("Project ID: {}, Users: {}", projectEntity.getId(), projectEntity.getUsers());

            boolean userHasAccess = projectEntity.getUsers().stream().anyMatch(user -> user.getId().equals(userId));
            if (userHasAccess) {
                Optional<Task> task = taskService.findTaskById(taskId);

                if (task.isPresent()) {
                    Task taskEntity = task.get();
                    taskEntity.setProject(projectEntity);
                    taskEntity.setUsers(userService.findAppUsersByTaskId(taskId));
                    if (taskEntity.getProject().getId().equals(projectId)) {
                        taskEntity.getUsers().add(userService.findUserById(userId).orElseThrow(() -> new RuntimeException("User not found")));
                        Task updatedTask = taskService.saveTask(taskEntity);
                        return ResponseEntity.ok(TaskMapper.toDto(updatedTask));
                    }
                }
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{projectId}/tasks/{taskId}/unassign")
    public ResponseEntity<TaskDTO> unassignSelfFromTask(@PathVariable Long projectId, @PathVariable Long taskId, Authentication authentication) {
        Long userId = userService.findUserByUsername(authentication.getName()).getId();
        Optional<Project> project = projectService.findProjectById(projectId);

        if (project.isPresent()) {
            Project projectEntity = project.get();
            //projectEntity.setUsers(userService.findAppUsersByProjectId(projectId));
            //projectEntity.setTasks(taskService.findTasksByProjectId(projectId));
            //logger.info("Project ID: {}, Users: {}", projectEntity.getId(), projectEntity.getUsers());

            boolean userHasAccess = projectEntity.getUsers().stream().anyMatch(user -> user.getId().equals(userId));
            if (userHasAccess) {
                Optional<Task> task = taskService.findTaskById(taskId);

                if (task.isPresent()) {
                    Task taskEntity = task.get();
                    taskEntity.setProject(projectEntity);
                    taskEntity.setUsers(userService.findAppUsersByTaskId(taskId));
                    if (taskEntity.getProject().getId().equals(projectId)) {
                        taskEntity.getUsers().removeIf(user -> user.getId().equals(userId));
                        Task updatedTask = taskService.saveTask(task.get());
                        return ResponseEntity.ok(TaskMapper.toDto(updatedTask));
                    }
                }
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{projectId}/users/{userId}")
    public ResponseEntity<ProjectDTO> addUserToProject(@PathVariable Long projectId, @PathVariable Long userId, Authentication authentication) {
        Long requesterId = userService.findUserByUsername(authentication.getName()).getId();
        Optional<Project> project = projectService.findProjectById(projectId);

        if (project.isPresent()) {
            Project projectEntity = project.get();
            //projectEntity.setUsers(userService.findAppUsersByProjectId(projectId));
            //projectEntity.setTasks(taskService.findTasksByProjectId(projectId));
            //logger.info("Project ID: {}, Users: {}", projectEntity.getId(), projectEntity.getUsers());

            boolean userHasAccess = projectEntity.getUsers().stream().anyMatch(user -> user.getId().equals(requesterId));
            if (userHasAccess) {
                projectEntity.getUsers().add(userService.findUserById(userId).orElseThrow(() -> new RuntimeException("User not found")));
                Project updatedProject = projectService.saveProject(projectEntity);
                return ResponseEntity.ok(ProjectMapper.toDto(updatedProject));
            } else {
                List<AppUser> usersWithAccess = userService.findAppUsersByProjectId(projectId);
                //logger.warn("User ID {} does not have access to project ID {}. Users with access: {}", requesterId, projectId, usersWithAccess.stream().map(AppUser::getUsername).collect(Collectors.toList()));
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

}
