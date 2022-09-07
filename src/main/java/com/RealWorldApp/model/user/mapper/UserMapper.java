package com.RealWorldApp.model.user.mapper;

import com.RealWorldApp.entity.User;
import com.RealWorldApp.model.user.UserDTOCreate;
import com.RealWorldApp.model.user.UserDTOResponse;

public class UserMapper {
    public static UserDTOResponse toUserDTOResponse(User user) {
        return UserDTOResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .build();

    }

    public static User toUser(UserDTOCreate userDTOCreate) {
        User user = User.builder()
                .username(userDTOCreate.getUsername())
                .email(userDTOCreate.getEmail())
                .password(userDTOCreate.getPassword())
                .build();
        return user;
    }
}
