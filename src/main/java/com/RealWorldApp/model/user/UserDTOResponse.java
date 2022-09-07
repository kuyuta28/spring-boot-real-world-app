package com.RealWorldApp.model.user;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserDTOResponse {

    private String email;
    private String token;
    private String username;
    private String bio;
    private String image;
}
