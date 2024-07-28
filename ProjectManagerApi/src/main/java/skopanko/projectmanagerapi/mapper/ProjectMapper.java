package skopanko.projectmanagerapi.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skopanko.projectmanagerapi.controller.ProjectController;
import skopanko.projectmanagerapi.dto.TaskDTO;
import skopanko.projectmanagerapi.dto.UserDTO;
import skopanko.projectmanagerapi.entity.Project;
import skopanko.projectmanagerapi.dto.ProjectDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProjectMapper {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    public static ProjectDTO toDto(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setTitle(project.getTitle());
        if (project.getUsers() != null) {
            dto.setUsers(project.getUsers().stream().map(UserMapper::toDto).toList());
        }
        if (project.getTasks() != null) {
            dto.setTasks(project.getTasks().stream().map(TaskMapper::toDto).toList());
        }
        return dto;
    }

    public static Project toEntity(ProjectDTO dto) {
        Project project = new Project();
        project.setId(dto.getId());
        project.setTitle(dto.getTitle());
        handleUserRelationships(project, dto.getUsers());
        handleTaskRelationships(project, dto.getTasks());
        return project;
    }

    private static void handleUserRelationships(Project project, List<UserDTO> userDTOs) {
        if (userDTOs != null) {
            project.setUsers(userDTOs.stream().map(UserMapper::toEntity).toList());
        }
    }

    private static void handleTaskRelationships(Project project, List<TaskDTO> taskDTOs) {
        if (taskDTOs != null) {
            project.setTasks(taskDTOs.stream().map(TaskMapper::toEntity).toList());
        }
    }
}
