package com.RealWorldApp.model;

import lombok.*;

@Setter
@ToString
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TokenPayload {
private String id;
private String email;
}
