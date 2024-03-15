package raster;

public class DepthBuffer implements Raster<Double> {
    private final double[][] buffer;
    private final int width, height;
    private double defaultValue;

    public DepthBuffer(int width, int height) {
        this.buffer = new double[width][height];
        this.width = width;
        this.height = height;
        this.defaultValue = Double.MAX_VALUE;
    }

    @Override
    public void clear() {
        for(int i =0;i<width;i++) for(int j = 0;j<height;j++) buffer[i][j] = defaultValue;
    }

    @Override
    public void setDefaultValue(Double value) {
        this.defaultValue = value;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public Double getValue(int x, int y) {
        return buffer[x][y];
    }

    @Override
    public void setValue(int x, int y, Double value) {
        buffer[x][y]=value;
    }
}
