package utils;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class DataUtils<T> {
    public static final String TRUE = "$T";
    public static final String FALSE = "$F";
    public static <T> DataUtils<T> UNDEFINED() {
        return new DataUtils<T>();
    }
}