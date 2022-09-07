package com.RealWorldApp.model.profiles;

import lombok.*;

@Setter
@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTOResponse {

    private String username;
    private String bio;
    private String image;
    private boolean following;
}
