package com.mipt.backwardscompatibility.Service;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
public class User {

    public User(@NotNull String login, @Nullable String name, @Nullable String surname, @Nullable String patronymic,
                @Nullable Integer age) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.age = age;
    }

    @NotNull
    String login;

    @Nullable
    String name;

    @Nullable
    String surname;

    @Nullable
    String patronymic;

    @Nullable
    Integer age;

}
