package com.RealWorldApp.model.user;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTOCreate {
    private String username;
    private String email;
    private String password;

}
