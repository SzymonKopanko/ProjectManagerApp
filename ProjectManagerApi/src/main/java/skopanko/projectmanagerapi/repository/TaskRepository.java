package skopanko.projectmanagerapi.repository;

import skopanko.projectmanagerapi.entity.AppUser;
import skopanko.projectmanagerapi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);

    @Query("SELECT t FROM Task t JOIN t.users u WHERE u.id = :userId")
    List<Task> findAllTasksByUserId(Long userId);

}

