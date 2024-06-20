package utils;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class DataUtil<T> {
    public static final String TRUE = "$T";
    public static final String FALSE = "$F";
    public static <T> DataUtil<T> UNDEFINED() {
        return new DataUtil<T>();
    }
}