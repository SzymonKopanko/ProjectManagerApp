package skopanko.projectmanagerapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskDTO {
    private Long id;
    @NotEmpty(message = "Title cannot be empty")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    private String description;
    private boolean completed;
    private Long projectId;
    private List<UserDTO> users;

}
