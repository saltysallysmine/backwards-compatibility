package com.mipt.backwardscompatibility.Controllers;

import com.mipt.backwardscompatibility.Service.Requests.RequestV1;
import com.mipt.backwardscompatibility.Service.Requests.RequestV2;
import com.mipt.backwardscompatibility.Service.Responses.ResponseV1;
import com.mipt.backwardscompatibility.Service.Responses.ResponseV2;
import com.mipt.backwardscompatibility.Service.User;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

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
        if (request.getLikeString() == null) {
            log.info("Get request with null likeString");
            return new ResponseV1(null);
        }
        Set<ResponseV1.UserV1> foundUsers = new java.util.HashSet<>(Set.of());
        ResponseV1 response = new ResponseV1(null);
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

//    @PostMapping("v2/get-users")
//    public ResponseV2 getUsersV2(@NotNull @RequestBody RequestV2 request) {
//    }
//
//    @PostMapping("v3/get-users")
//    public Set<User> getUsersV3(@NotNull @RequestBody GetUsersRequest request) { }
//
//    @PostMapping("v4/get-users")
//    public Set<User> getUsersV4(@NotNull @RequestBody GetUsersRequest request) { }
//
//    @PostMapping("v5/get-users")
//    public Set<User> getUsersV5(@NotNull @RequestBody GetUsersRequest request) {
//
//    }

}
