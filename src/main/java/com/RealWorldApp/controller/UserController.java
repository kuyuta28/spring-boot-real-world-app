package com.RealWorldApp.controller;

import com.RealWorldApp.entity.User;
import com.RealWorldApp.exception.custom.CustomBadRequestException;
import com.RealWorldApp.exception.custom.CustomNotFoundException;
import com.RealWorldApp.model.user.UserDTOCreate;
import com.RealWorldApp.model.user.UserDTOLoginRequest;
import com.RealWorldApp.model.user.UserDTOResponse;
import com.RealWorldApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/login")
    public Map<String, UserDTOResponse> login(@RequestBody Map<String, UserDTOLoginRequest> loginRequestMap) throws CustomBadRequestException {
        return userService.authenticate(loginRequestMap);
    }

    @PostMapping("/users")
    public Map<String, UserDTOResponse> registerUser(@RequestBody Map<String, UserDTOCreate> userDTOCreateMap){
        return userService.registerUser(userDTOCreateMap);
    }

    @GetMapping("/user")
    public Map<String, UserDTOResponse> getCurrentUser() throws CustomNotFoundException {
        return userService.getCurrentUser();
    }

}
