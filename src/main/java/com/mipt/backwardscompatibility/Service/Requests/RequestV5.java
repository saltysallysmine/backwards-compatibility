package com.mipt.backwardscompatibility.Service.Requests;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;

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

    public RequestV5(@Nullable String regexString, @Nullable String surname, @Nullable Integer leftAgeBorder,
              @Nullable Integer rightAgeBorder) {
        this.likeString = null;
        this.regexString = regexString;
        this.surname = surname;
        this.leftAgeBorder = leftAgeBorder;
        this.rightAgeBorder = rightAgeBorder;
    }

}
