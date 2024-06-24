package utils;

import java.util.ArrayList;
import java.util.List;

public class TupleUtils {

    public static <T> List<List<T>> generateKTuples(int k, List<T> values) {
        List<List<T>> results = new ArrayList<>();
        List<T> extendedValues = new ArrayList<>(values);
        extendedValues.add(null); // 注意这里的类型转换
        generateKTuplesHelper(k, extendedValues, new ArrayList<>(), results);
        return results;
    }

    public static <T> List<List<T>> generateKTuplesWithoutNull(int k, List<T> values) {
        List<List<T>> results = new ArrayList<>();
        List<T> extendedValues = new ArrayList<>(values);
        generateKTuplesHelper(k, extendedValues, new ArrayList<>(), results);
        return results;
    }

    public static <T> List<List<T>> generateCartesian(List<List<T>> lists) {
        List<List<T>> results = new ArrayList<>();
        generateCartesianHelper(lists, 0, new ArrayList<>(), results);
        return results;
    }

    private static <T> void generateCartesianHelper(List<List<T>> lists, int depth, List<T> current,
            List<List<T>> results) {
        if (depth == lists.size()) {
            results.add(new ArrayList<>(current));
            return;
        }

        for (T element : lists.get(depth)) {
            current.add(element);
            generateCartesianHelper(lists, depth + 1, current, results);
            current.remove(current.size() - 1);
        }
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
