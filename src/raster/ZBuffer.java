package raster;

import transforms.Col;

public class ZBuffer {
    private final Raster<Col> imageBuffer;
    private final Raster<Double> depthBuffer;

    public ZBuffer(Raster<Col> imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
    }
    public void setDefaultValues()
    {
        depthBuffer.clear();
    }

    public void setPixelWithZTest(int x, int y, double z, Col col) {
        if(x<0||x>=imageBuffer.getWidth()) {
            //System.out.println("x="+x);
            return;
        }
        if(y<0||y>=imageBuffer.getHeight())
        {
           // System.out.println("y="+z);
            return;
        }
        // TODO: načtu hodnotu z depth bufferu na souřadnici x, y
        if(z>= depthBuffer.getValue(x,y)) {
            return;
        }
        // TODO: kontrola, jestli jsem dostal validní hodnotu
        // TODO: kontrola, jestli nové z  < staré z
        // TODO: pokud platí: 1) Obarvit 2) zapsat nové z do depth bufferu
        depthBuffer.setValue(x,y,z);
        imageBuffer.setValue(x, y, col);
    }

    // TODO: odebrat, jen pro debug
    public Raster<Col> getImageBuffer() {
        return imageBuffer;
    }

    public int getWidth() {
        return imageBuffer.getWidth();
    }

    public int getHeight() {return imageBuffer.getHeight();}
}
