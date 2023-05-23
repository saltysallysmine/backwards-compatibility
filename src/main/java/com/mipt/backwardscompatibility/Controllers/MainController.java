package com.mipt.backwardscompatibility.Controllers;

import com.mipt.backwardscompatibility.Service.Requests.RequestV1;
import com.mipt.backwardscompatibility.Service.Requests.RequestV4;
import com.mipt.backwardscompatibility.Service.Responses.ResponseV1;
import com.mipt.backwardscompatibility.Service.Responses.ResponseV2;
import com.mipt.backwardscompatibility.Service.Responses.ResponseV3;
import com.mipt.backwardscompatibility.Service.User;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/backwards-compatibility")
public class MainController {

    Set<User> usersRepository;

    public void setUsersRepository(@NotNull Set<User> newRepo) {
        this.usersRepository = newRepo;
    }

    public String getRegexOf(@NotNull String likeString) {
        return likeString
                .replaceAll("\\*", "\\\\*")
                .replaceAll("\\.", "\\\\.")
                .replaceAll("(?=[^\\\\])%", ".*")
                .replaceAll("(?=[^\\\\])_", ".");
    }

    @PostMapping("v1/get-users")
    public ResponseV1 getUsersV1(@NotNull @RequestBody RequestV1 request) {
        Set<ResponseV1.UserV1> foundUsers = new java.util.HashSet<>(Set.of());
        ResponseV1 response = new ResponseV1(null);
        // null requestedPattern -> send all users
        if (request.getLikeString() == null) {
            log.info("Get request with null likeString");
            usersRepository.forEach(user -> {
                foundUsers.add(new ResponseV1.UserV1(user.getLogin()));
            });
            return new ResponseV1(foundUsers);
        }
        // not null requestedPattern
        String requestedPattern = getRegexOf(request.getLikeString());
        log.info("Get request with likeString=" + request.getLikeString() + " converted as " + requestedPattern);
        usersRepository.forEach(user -> {
            if (user.getLogin().matches(requestedPattern)) {
                foundUsers.add(new ResponseV1.UserV1(user.getLogin()));
            }
        });
        response.setFoundUsers(foundUsers);
        return response;
    }

    @PostMapping("v2/get-users")
    public ResponseV2 getUsersV2(@NotNull @RequestBody RequestV1 request) {
        Set<ResponseV2.UserV2> foundUsers = new java.util.HashSet<>(Set.of());
        ResponseV2 response = new ResponseV2(usersRepository.size(), null);
        // null requestedPattern -> send all users
        if (request.getLikeString() == null) {
            log.info("Get request with null likeString");
            usersRepository.forEach(user -> {
                foundUsers.add(new ResponseV2.UserV2(user.getLogin()));
            });
            return new ResponseV2(usersRepository.size(), foundUsers);
        }
        // not null requestedPattern
        String requestedPattern = getRegexOf(request.getLikeString());
        log.info("Get request with likeString=" + request.getLikeString() + " converted as " + requestedPattern);
        usersRepository.forEach(user -> {
            if (user.getLogin().matches(requestedPattern)) {
                foundUsers.add(new ResponseV2.UserV2(user.getLogin()));
            }
        });
        response.setFoundUsers(foundUsers);
        return response;
    }

    @PostMapping("v3/get-users")
    public ResponseV3 getUsersV3(@NotNull @RequestBody RequestV1 request) {
        Set<ResponseV3.UserV3> foundUsers = new java.util.HashSet<>(Set.of());
        ResponseV3 response = new ResponseV3(usersRepository.size(), null);
        // null requestedPattern -> send all users
        if (request.getLikeString() == null) {
            log.info("Get request with null likeString");
            usersRepository.forEach(user -> {
                foundUsers.add(new ResponseV3.UserV3(
                        user.getLogin(),
                        user.getName(),
                        user.getSurname(),
                        user.getPatronymic()));
            });
            return new ResponseV3(usersRepository.size(), foundUsers);
        }
        // not null requestedPattern
        String requestedPattern = getRegexOf(request.getLikeString());
        log.info("Get request with likeString=" + request.getLikeString() + " converted as " + requestedPattern);
        usersRepository.forEach(user -> {
            if (user.getLogin().matches(requestedPattern)) {
                foundUsers.add(new ResponseV3.UserV3(
                        user.getLogin(),
                        user.getName(),
                        user.getSurname(),
                        user.getPatronymic()));
            }
        });
        response.setFoundUsers(foundUsers);
        return response;
    }

    public void addMatchesUsersToSetV4(Set<ResponseV3.UserV3> usersContainer, String pattern) {
        usersRepository.forEach(user -> {
            if (user.getLogin().matches(pattern)) {
                usersContainer.add(new ResponseV3.UserV3(
                        user.getLogin(),
                        user.getName(),
                        user.getSurname(),
                        user.getPatronymic()));
            }
        });
    }

    @PostMapping("v4/get-users")
    public ResponseV3 getUsersV4(@NotNull @RequestBody RequestV4 request) {
        Set<ResponseV3.UserV3> foundUsers = new java.util.HashSet<>(Set.of());
        ResponseV3 response = new ResponseV3(usersRepository.size(), null);
        // null all of requestedPatterns -> send all users
        if (request.getLikeString() == null && request.getRegexString() == null) {
            log.info("Get request with null likeString and null regexString");
            addMatchesUsersToSetV4(foundUsers, ".*");
            return new ResponseV3(usersRepository.size(), foundUsers);
        }
        // null regexString
        if (request.getRegexString() == null) {
            String requestedLike = getRegexOf(request.getLikeString());
            log.info("Get request with likeString=" + request.getLikeString() + " converted as " + requestedLike);
            addMatchesUsersToSetV4(foundUsers, requestedLike);
            response.setFoundUsers(foundUsers);
            return response;
        }
        // not null regexString
        String requestedRegex = request.getRegexString();
        log.info("Get request with regexString=" + request.getRegexString());
        addMatchesUsersToSetV4(foundUsers, requestedRegex);
        response.setFoundUsers(foundUsers);
        return response;
    }

//    @PostMapping("v5/get-users")
//    public Set<User> getUsersV5(@NotNull @RequestBody GetUsersRequest request) {
//
//    }

}
