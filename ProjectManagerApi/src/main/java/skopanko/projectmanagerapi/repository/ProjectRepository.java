package skopanko.projectmanagerapi.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import skopanko.projectmanagerapi.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByTitleContaining(String name);

    @Query("SELECT t FROM Project t JOIN t.users u WHERE u.id = :userId")
    List<Project> findAllProjectsByUserId(Long userId);


    @NotNull
    Optional<Project> findById(Long id);



}
