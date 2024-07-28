package skopanko.projectmanagerapi.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skopanko.projectmanagerapi.controller.ProjectController;
import skopanko.projectmanagerapi.dto.UserDTO;
import skopanko.projectmanagerapi.entity.AppUser;

public class UserMapper {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);


    public static UserDTO toDto(AppUser user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPassword(null);
        return dto;
    }

    public static AppUser toEntity(UserDTO dto) {
        AppUser user = new AppUser();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }
}
