package com.RealWorldApp.service.impl;

import com.RealWorldApp.entity.User;
import com.RealWorldApp.exception.custom.CustomBadRequestException;
import com.RealWorldApp.exception.custom.CustomNotFoundException;
import com.RealWorldApp.model.CustomError;
import com.RealWorldApp.model.profiles.ProfileDTOResponse;
import com.RealWorldApp.model.user.UserDTOCreate;
import com.RealWorldApp.model.user.UserDTOLoginRequest;
import com.RealWorldApp.model.user.UserDTOResponse;
import com.RealWorldApp.model.user.mapper.UserMapper;
import com.RealWorldApp.repository.UserRepository;
import com.RealWorldApp.service.UserService;
import com.RealWorldApp.util.JWTTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JWTTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Map<String, UserDTOResponse> authenticate(Map<String, UserDTOLoginRequest> loginRequestMap) throws CustomBadRequestException {
        UserDTOLoginRequest userDTOLoginRequest = loginRequestMap.get("user");
        Optional<User> userOptional = userRepository.findByEmail(userDTOLoginRequest.getEmail());
        boolean isAuthen = false;
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(userDTOLoginRequest.getPassword(), user.getPassword())) {
                isAuthen = true;
            }
        }
        if (!isAuthen) {
            throw new CustomBadRequestException(
                    CustomError.builder()
                            .code("400")
                            .message("Username or Password incorrect")
                            .build()
            );
        } else {
            return buildDTOResponse(userOptional.get());
        }

    }

    @Override
    public Map<String, UserDTOResponse> registerUser(Map<String, UserDTOCreate> userDTOCreateMap) {
        UserDTOCreate userDTOCreate = userDTOCreateMap.get("user");
        User user = UserMapper.toUser(userDTOCreate);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return buildDTOResponse(user);
    }

    @Override
    public Map<String, UserDTOResponse> getCurrentUser() throws CustomNotFoundException {
        User userLoggedIn = getUserLoggedIn();
        if (userLoggedIn != null) {
            return buildDTOResponse(userLoggedIn);
        }
        throw new CustomNotFoundException(CustomError.builder()
                .code("404")
                .message("user not found")
                .build());
    }

    public User getUserLoggedIn() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println("principal : " + principal.toString());
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
//            System.out.println("Email is : " + email);
            User user = userRepository.findByEmail(email).get();
//            System.out.println(user);
            return user;
        }
        return null;
    }

    @Override
    public Map<String, ProfileDTOResponse> getProfile(String username) throws CustomNotFoundException {
        User userLoggedIn = getUserLoggedIn();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new CustomNotFoundException(CustomError.builder()
                    .code("404")
                    .message("User not found")
                    .build());
        }
        User user = userOptional.get();
        Set<User> follower = user.getFollowers();
        boolean isFollowing = false;
        if (follower != null) {
            for (User u :
                    follower) {
                if (u.getId().equals(userLoggedIn.getId())) {
                    isFollowing = true;
                    break;
                }
            }
        }
        return buildProfileResponse(userOptional.get(), isFollowing);
    }

    public boolean checkFollowing(String username) throws CustomNotFoundException {
        User userLoggedIn = getUserLoggedIn();
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = isUserExist(userOptional);
        Set<User> follower = user.getFollowers();
        boolean isFollowing = false;
        if (follower != null) {
            for (User u :
                    follower) {
                if (u.getId().equals(userLoggedIn.getId())) {
                    isFollowing = true;
                    break;
                }
            }
        }
        return isFollowing;
    }

    public User isUserExist(Optional<User> userOptional) throws CustomNotFoundException {
        if (userOptional.isEmpty()) {
            throw new CustomNotFoundException(CustomError.builder()
                    .code("404")
                    .message("User not found")
                    .build());
        }
        return userOptional.get();

    }

    @Override
    public Map<String, ProfileDTOResponse> followUser(String username) throws CustomNotFoundException {
        User userLoggedIn = getUserLoggedIn();
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = isUserExist(userOptional);
        boolean isFollowing = checkFollowing(username);
        if (!isFollowing) {
            isFollowing = true;
            if (user.getFollowers() == null) {
                Set<User> userList = new HashSet<>();
                userList.add(userLoggedIn);
                user.setFollowers(userList);
            } else {
                user.getFollowers().add(userLoggedIn);
            }
            userRepository.save(user);
//            if(userLoggedIn.getFollowing()==null){
//                Set<User> userList1 = new HashSet<>();
//                userList1.add(user);
//                userLoggedIn.setFollowing(userList1);
//            }else {
//                userLoggedIn.getFollowing().add(user);
//            }
//             userRepository.save(userLoggedIn);
        }
        return buildProfileResponse(userOptional.get(), isFollowing);
    }

    @Override
    public Map<String, ProfileDTOResponse> unfollowUser(String username) throws CustomNotFoundException {
        User userLoggedIn = getUserLoggedIn();
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = isUserExist(userOptional);
        boolean isFollowing = checkFollowing(username);
        if (isFollowing) {
            if (user.getFollowers().size() == 1) {
                user.setFollowers(null);
            } else {
                for (User u :
                        user.getFollowers()) {
                    if (u.equals(userLoggedIn)) {
                        user.getFollowers().remove(u);
                        isFollowing = false;
                    }
                }
            }
            userRepository.save(user);
        }
        return buildProfileResponse(userOptional.get(), isFollowing);

    }

    private Map<String, ProfileDTOResponse> buildProfileResponse(User user, boolean isFollowing) {
        Map<String, ProfileDTOResponse> wrapper = new HashMap<>();
        ProfileDTOResponse profileDTOResponse = ProfileDTOResponse.builder()
                .bio(user.getBio())
                .image(user.getImage())
                .username(user.getUsername())
                .following(isFollowing)
                .build();
        wrapper.put("profile", profileDTOResponse);
        return wrapper;
    }

    private Map<String, UserDTOResponse> buildDTOResponse(User user) {
        Map<String, UserDTOResponse> wrapper = new HashMap<>();
        UserDTOResponse userDTOResponse = UserMapper.toUserDTOResponse(user);
        userDTOResponse.setToken(jwtTokenUtil.generateToken(user, 60 * 60 * 24L));
        wrapper.put("user", userDTOResponse);
        return wrapper;
    }
}
