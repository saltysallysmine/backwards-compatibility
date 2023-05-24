package com.mipt.backwardscompatibility.Service.Requests;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class RequestV5 {

    // this parameter added for backward compatibility
    @Nullable
    String likeString;

    @Nullable
    String regexString;

    @Nullable
    String surname;

    @Nullable
    Integer leftAgeBorder;

    @Nullable
    Integer rightAgeBorder;

}
