package skopanko.projectmanagerapi.entity;

import lombok.*;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;

    @ManyToMany(mappedBy = "users")
    private List<Project> projects;

    @ManyToMany(mappedBy = "users")
    private List<Task> tasks;
}
