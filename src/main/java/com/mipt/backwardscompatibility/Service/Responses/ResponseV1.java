package com.mipt.backwardscompatibility.Service.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@Data
@AllArgsConstructor
public class ResponseV1 {

    @Data
    @AllArgsConstructor
    public static class UserV1 {
        @Nullable
        String login;
    }

    @Nullable
    Set<UserV1> foundUsers;

}
