package com.mipt.backwardscompatibility.Service.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class RequestV4 {

    @Nullable
    String likeString;

    @Nullable
    String regexString;

}
