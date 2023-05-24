package com.mipt.backwardscompatibility.Controllers;

import com.mipt.backwardscompatibility.Service.Requests.RequestV1;
import com.mipt.backwardscompatibility.Service.Requests.RequestV4;
import com.mipt.backwardscompatibility.Service.Requests.RequestV5;
import com.mipt.backwardscompatibility.Service.Responses.ResponseV1;
import com.mipt.backwardscompatibility.Service.Responses.ResponseV2;
import com.mipt.backwardscompatibility.Service.Responses.ResponseV3;
import com.mipt.backwardscompatibility.Service.User;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/backwards-compatibility")
public class MainController {

    Set<User> usersRepository = new HashSet<>(Set.of(
                new User("ANDREY", "Andrey", "Shinkarenko", "Vladimirovich", 18),
                new User("ANDreY", "Andrey", "Menelaevich", null, 19),
                new User("KONSTANTIN", "Konstantin", "Bolshikov", "Andreevich", 19),
                new User("VALERY", "Valery", "Bergman", "Dmitrievich", 18)));

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

    @GetMapping("v1/get-users")
    @ResponseBody
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

    @GetMapping("v2/get-users")
    @ResponseBody
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

    @GetMapping("v3/get-users")
    @ResponseBody
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

    @GetMapping("v4/get-users")
    @ResponseBody
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

    public void addMatchesUsersToSetV5(Set<ResponseV3.UserV3> usersContainer, String pattern, boolean isSurname) {
        Set<ResponseV3.UserV3> foundUsers = new HashSet<>(Set.of());
        usersRepository.forEach(user -> {
            if (isSurname && Objects.equals(user.getSurname(), pattern) ||
                    !isSurname && user.getLogin().matches(pattern)) {
                foundUsers.add(new ResponseV3.UserV3(
                        user.getLogin(),
                        user.getName(),
                        user.getSurname(),
                        user.getPatronymic()));
            }
        });
        usersContainer.retainAll(foundUsers);
    }

    public void addMatchesUsersToSetV5(
            Set<ResponseV3.UserV3> usersContainer, @Nullable Integer leftBorder, @Nullable Integer rightBorder) {
        Set<ResponseV3.UserV3> foundUsers = new HashSet<>(Set.of());
        usersRepository.forEach(user -> {
            boolean userMatches = true;
            if (user.getAge() == null) {
                userMatches = false;
            } else if (leftBorder != null && user.getAge() < leftBorder) {
                userMatches = false;
            } else if (rightBorder != null && user.getAge() > rightBorder) {
                userMatches = false;
            }
            if (userMatches) {
                foundUsers.add(new ResponseV3.UserV3(
                        user.getLogin(),
                        user.getName(),
                        user.getSurname(),
                        user.getPatronymic()));
            }
        });
        usersContainer.retainAll(foundUsers);
    }

    public void addAllUsersToSetV5(Set<ResponseV3.UserV3> usersContainer) {
        usersRepository.forEach(user -> {
            usersContainer.add(new ResponseV3.UserV3(
                    user.getLogin(),
                    user.getName(),
                    user.getSurname(),
                    user.getPatronymic()));
        });
    }

    @GetMapping("v5/get-users")
    @ResponseBody
    public ResponseV3 getUsersV5(@NotNull @RequestBody RequestV5 request) {
        Set<ResponseV3.UserV3> foundUsers = new java.util.HashSet<>(Set.of());
        ResponseV3 response = new ResponseV3(usersRepository.size(), null);
        // If all of requestedPatterns are null we will don't go to any if
        // and return full list of users. Else we will retain it with each
        // found users set and then return.
        log.info("Get V5 request");
        addAllUsersToSetV5(foundUsers);
        log.info(foundUsers.toString());
        // process likeString
        if (request.getLikeString() != null) {
            String requestedLike = getRegexOf(request.getLikeString());
            log.info("Process regexString=" + request.getRegexString());
            addMatchesUsersToSetV5(foundUsers, requestedLike, false);
        }
        log.info(foundUsers.toString());
        // process regexString
        if (request.getRegexString() != null) {
            String requestedRegex = request.getRegexString();
            log.info("Process regexString=" + request.getRegexString());
            addMatchesUsersToSetV5(foundUsers, requestedRegex, false);
        }
        log.info(foundUsers.toString());
        // process age borders
        if (request.getLeftAgeBorder() != null || request.getRightAgeBorder() != null) {
            Integer requestedLeftAgeBorder = request.getLeftAgeBorder();
            Integer requestedRightAgeBorder = request.getRightAgeBorder();
            log.info("Process age borders");
            addMatchesUsersToSetV5(foundUsers, requestedLeftAgeBorder, requestedRightAgeBorder);
        }
        log.info(foundUsers.toString());
        // process surname
        if (request.getSurname() != null) {
            String requestedSurname = request.getSurname();
            log.info("Process surname=" + request.getSurname());
            addMatchesUsersToSetV5(foundUsers, requestedSurname, true);
        }
        log.info(foundUsers.toString());
        response.setFoundUsers(foundUsers);
        return response;
    }

}
