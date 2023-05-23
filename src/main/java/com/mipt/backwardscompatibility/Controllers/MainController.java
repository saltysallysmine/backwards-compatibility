package com.mipt.backwardscompatibility.Controllers;

import com.mipt.backwardscompatibility.Service.GetUsersRequest;
import com.mipt.backwardscompatibility.Service.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/backwards-compatibility")
public class MainController {

    Set<User> usersRepository = Set.of(
            new User("ANDREY", "Andrey", "Shinkarenko", "Vladimirovich", 18),
            new User("KONSTANTIN", "Konstantin", "Bolshikov", "Andreevich", 19),
            new User("VALERY", "Valery", "Bergman", "Dmitrievich", 18));

//    @PostMapping("v1/get-users")
//    public Set<User> GetUsersV1(@NotNull @RequestBody GetUsersRequest request) { }
//
//    @PostMapping("v2/get-users")
//    public Set<User> GetUsersV2(@NotNull @RequestBody GetUsersRequest request) { }
//
//    @PostMapping("v3/get-users")
//    public Set<User> GetUsersV3(@NotNull @RequestBody GetUsersRequest request) { }
//
//    @PostMapping("v4/get-users")
//    public Set<User> GetUsersV4(@NotNull @RequestBody GetUsersRequest request) { }
//
//    @PostMapping("v5/get-users")
//    public Set<User> GetUsersV5(@NotNull @RequestBody GetUsersRequest request) {
//
//    }

}
