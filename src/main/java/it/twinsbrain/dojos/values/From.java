package it.twinsbrain.dojos.values;

public record From(int x, int y) {
    public static From of(int x, int y) {return new From(x, y);}
}
