package com.mipt.backwardscompatibility.Service;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class GetUsersRequest {

    @Nullable
    String likeString;

    @Nullable
    String regexString;

    @Nullable
    String surname;

    @Nullable
    String leftAgeBorder;

    @Nullable
    String rightAgeBorder;

}
