package com.RealWorldApp.controller;

import com.RealWorldApp.exception.custom.CustomNotFoundException;
import com.RealWorldApp.model.profiles.ProfileDTOResponse;
import com.RealWorldApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles/{username}")
public class ProfileController {

    private final UserService userService;

    @GetMapping("")
    public Map<String, ProfileDTOResponse> getProfile(@PathVariable String username) throws CustomNotFoundException {
        return userService.getProfile(username);
    }

    @PostMapping ("/follow")
    public Map<String, ProfileDTOResponse> followUser(@PathVariable String username) throws CustomNotFoundException {
        return userService.followUser(username);
    }

    @DeleteMapping ("/follow")
    public Map<String, ProfileDTOResponse> unfollowUser(@PathVariable String username) throws CustomNotFoundException {
        return userService.unfollowUser(username);
    }

}
