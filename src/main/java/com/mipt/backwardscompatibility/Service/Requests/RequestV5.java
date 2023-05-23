package com.mipt.backwardscompatibility.Service.Requests;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestV5 {

    @Nullable
    String regexString;

    @Nullable
    String surname;

    @Nullable
    Integer leftAgeBorder;

    @Nullable
    Integer rightAgeBorder;

}
