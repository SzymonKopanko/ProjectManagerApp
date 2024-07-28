package skopanko.projectmanagerapi.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skopanko.projectmanagerapi.controller.ProjectController;
import skopanko.projectmanagerapi.dto.TaskDTO;
import skopanko.projectmanagerapi.dto.UserDTO;
import skopanko.projectmanagerapi.entity.Project;
import skopanko.projectmanagerapi.entity.Task;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskMapper {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);


    public static TaskDTO toDto(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setCompleted(task.isCompleted());
        if (task.getUsers() != null) {
            dto.setUsers(task.getUsers().stream().map(UserMapper::toDto).toList());
        }
        dto.setProjectId(task.getProject().getId());
        return dto;
    }

    public static Task toEntity(TaskDTO dto) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCompleted(dto.isCompleted());
        handleUserRelationships(task, dto.getUsers());
        return task;
    }

    private static void handleUserRelationships(Task task, List<UserDTO> userDTOs) {
        if (userDTOs != null) {
            task.setUsers(userDTOs.stream().map(UserMapper::toEntity).toList());
        }
    }
}
