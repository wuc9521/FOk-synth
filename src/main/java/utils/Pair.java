package utils;

public class Pair<FType, SType> {
    public FType first;
    public SType second;

    public Pair(FType first, SType second) {
        this.first = first;
        this.second = second;
    }

    public String toString() {
        return "(" + first.toString() + ", " + second.toString() + ")";
    }

    public int hashCode() {
        return first.hashCode() ^ second.hashCode();
    }
}
