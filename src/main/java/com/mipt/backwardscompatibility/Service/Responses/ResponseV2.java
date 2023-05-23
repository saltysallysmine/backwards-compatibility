package com.mipt.backwardscompatibility.Service.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@Data
@AllArgsConstructor
public class ResponseV2 {

    @NotNull
    Integer usersCount;

    @Data
    @AllArgsConstructor
    public static class UserV2 {
        @Nullable
        String login;
    }

    @Nullable
    Set<UserV2> foundUsers;

}
