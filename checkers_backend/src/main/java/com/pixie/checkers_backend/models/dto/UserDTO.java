package com.pixie.checkers_backend.models.dto;

import com.pixie.checkers_backend.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;



@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDTO {

    @EqualsAndHashCode.Include
    private String username;
    private String email;

    private Boolean online;
    private Integer points;
    private String country;
    private String language;
    private Byte[] image;

    public static UserDTO mapToUserDTO(User user){
        return new UserDTO(user.getUsername(), user.getEmail(), user.getOnline(), user.getPoints(), user.getCountry(), user.getLanguage(), user.getImage());
    }

}
