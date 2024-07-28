package skopanko.projectmanagerapi.repository;

import org.springframework.data.jpa.repository.Query;
import skopanko.projectmanagerapi.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByEmail(String email);

    Optional<AppUser> findByUsername(String username);

    @Query("SELECT t FROM AppUser t JOIN t.projects u WHERE u.id = :projectId")
    List<AppUser> findAllUsersByProjectId(Long projectId);

    @Query("SELECT t FROM AppUser t JOIN t.tasks u WHERE u.id = :taskId")
    List<AppUser> findAllUsersByTaskId(Long taskId);



}
