package skopanko.projectmanagerapi.service;

import skopanko.projectmanagerapi.entity.Task;
import skopanko.projectmanagerapi.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Transactional
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    public List<Task> findTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }


    @Transactional(readOnly = true)
    public List<Task> findTasksByUserId(Long userId) {
        return taskRepository.findAllTasksByUserId(userId);
    }
}
