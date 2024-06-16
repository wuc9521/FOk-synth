package utils;

import java.util.ArrayList;
import java.util.List;

public class TupleGenerator {

    public static <T> List<List<T>> generateKTuples(int k, List<T> values) {
        List<List<T>> results = new ArrayList<>();
        generateKTuplesHelper(k, values, new ArrayList<>(), results);
        return results;
    }

    private static <T> void generateKTuplesHelper(int k, List<T> values, List<T> current, List<List<T>> results) {
        if (current.size() == k) {
            results.add(new ArrayList<>(current));
            return;
        }

        for (T value : values) {
            current.add(value);
            generateKTuplesHelper(k, values, current, results);
            current.remove(current.size() - 1);
        }
    }
}
