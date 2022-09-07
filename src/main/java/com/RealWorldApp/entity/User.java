package com.RealWorldApp.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("User")

public class User {
    @Id
    private String id;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String username;
    private String password;
    private String bio;
    private String image;

    private Set<User> followers;
    private Set<User> following;

    @Override
    public boolean equals(Object ob) {
        if (ob == null) {
            return false;
        }
        if (this == ob) {
            return true;
        }
        if (ob instanceof User) {
            User other = (User) ob;
            return this.id.equals(other.getId());
        }
        return false;
    }

}
