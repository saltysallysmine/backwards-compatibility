package com.mipt.backwardscompatibility.Service.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@Data
@AllArgsConstructor
public class ResponseV3 {

    @NotNull
    Integer usersCount;

    @Data
    @AllArgsConstructor
    public static class UserV3 {

        @Nullable
        String login;

        @Nullable
        String name;

        @Nullable
        String surname;

        @Nullable
        String patronymic;

    }

    @Nullable
    Set<UserV3> foundUsers;

}
