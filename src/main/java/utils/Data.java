package utils;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class Data<T> {
    public static final String TRUE = "$T";
    public static final String FALSE = "$F";
    public static <T> Data<T> UNDEFINED() {
        return new Data<T>();
    }
}