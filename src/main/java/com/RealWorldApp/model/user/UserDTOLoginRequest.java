package com.RealWorldApp.model.user;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserDTOLoginRequest {
    private String email;
    private String password;
}
