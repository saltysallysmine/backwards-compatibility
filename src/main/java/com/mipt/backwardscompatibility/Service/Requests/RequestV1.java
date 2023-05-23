package com.mipt.backwardscompatibility.Service.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class RequestV1 {
    @Nullable
    String likeString;
}
