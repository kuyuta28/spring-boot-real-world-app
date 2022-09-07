package com.RealWorldApp.service;

import com.RealWorldApp.exception.custom.CustomBadRequestException;
import com.RealWorldApp.exception.custom.CustomNotFoundException;
import com.RealWorldApp.model.profiles.ProfileDTOResponse;
import com.RealWorldApp.model.user.UserDTOCreate;
import com.RealWorldApp.model.user.UserDTOLoginRequest;
import com.RealWorldApp.model.user.UserDTOResponse;

import java.util.Map;

public interface UserService {
    Map<String, UserDTOResponse> authenticate(Map<String, UserDTOLoginRequest> loginRequestMap) throws CustomBadRequestException;

    Map<String, UserDTOResponse> registerUser(Map<String, UserDTOCreate> userDTOCreateMap);

    Map<String, UserDTOResponse> getCurrentUser() throws CustomNotFoundException;

    Map<String, ProfileDTOResponse> getProfile(String username) throws CustomNotFoundException;

    Map<String, ProfileDTOResponse> followUser(String username) throws CustomNotFoundException;

    Map<String, ProfileDTOResponse> unfollowUser(String username) throws CustomNotFoundException;
}
