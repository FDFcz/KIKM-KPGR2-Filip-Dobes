package raster;

import transforms.Col;

public interface Interpolative<T> {
    public T add(final T c);

    public T mul(final double x);

    public T mul(final T c);

}
