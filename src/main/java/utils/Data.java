package utils;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class Data<T> {
    public static <T> Data<T> UNDEFINED() {
        return new Data<T>();
    }
}