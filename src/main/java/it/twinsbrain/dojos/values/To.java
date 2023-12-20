package it.twinsbrain.dojos.values;

public record To(int x, int y) {
    public static To of(int x, int y) {return new To(x, y);}
}
