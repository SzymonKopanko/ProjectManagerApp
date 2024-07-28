package skopanko.projectmanagerapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skopanko.projectmanagerapi.controller.ProjectController;
import skopanko.projectmanagerapi.entity.AppUser;
import skopanko.projectmanagerapi.entity.Project;
import skopanko.projectmanagerapi.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);


    @Transactional(readOnly = true)
    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    @Transactional
    public List<Project> findAllProjectsByUserId(Long userId) {
        return projectRepository.findAllProjectsByUserId(userId);
    }


    @Transactional(readOnly = true)
    public Optional<Project> findProjectById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            logger.info("Found project with id: " + id);
            logger.info("Found project users: " + project.get().getUsers());
            logger.info("Found project tasks: " + project.get().getTasks());
        }
        return projectRepository.findById(id);
    }


    @Transactional
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }


    @Transactional
    public Project updateProject(Project project) {
        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Project> findProjectsByNameContaining(String name) {
        return projectRepository.findByTitleContaining(name);
    }
}
