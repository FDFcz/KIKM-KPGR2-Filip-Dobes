package raster;

import solid.Vertex;
import transforms.Col;

import javax.swing.plaf.synth.Region;
import java.util.concurrent.atomic.AtomicReference;

public class TriangleRasterizer extends ObjectRasterizer {
    //private final ZBuffer zBuffer;

    public TriangleRasterizer(ZBuffer zBuffer) {
        super(zBuffer);
    }

    public void rasterize(Vertex a, Vertex b, Vertex c) {
        // TODO: seřadit vrcholy pod y od min
        if (c.getPosition().getY() < b.getPosition().getY())
        {
            Vertex temp = new Vertex(c);
            c = new Vertex(b);
            b = new Vertex(temp);
        }
        if(b.getPosition().getY() < a.getPosition().getY())
        {
            Vertex temp = new Vertex(b);
            b = new Vertex(a);
            a = new Vertex(temp);
        }
        if (c.getPosition().getY() < b.getPosition().getY())
        {
            Vertex temp = new Vertex(c);
            c = new Vertex(b);
            b = new Vertex(temp);
        }

        int xA = (int) a.getPosition().getX();
        int yA = (int) a.getPosition().getY();
        int zA = (int) a.getW();
        int yB = (int) b.getPosition().getY();
        int xB = (int) b.getPosition().getX();
        int zB = (int) b.getW();
        int yC = (int) c.getPosition().getY();
        int xC = (int) c.getPosition().getX();
        int zC = (int) c.getW();

        // Cyklus od A do B (první část)
        int minY = Math.min(yB, zBuffer.getHeight());
        int maxY = Math.max(yA, 0);
        for (int y = maxY; y <= minY; y++) {
            // V1
            double t1 = (y - yA) / (double) (yB - yA);
            int x1 = (int) Math.round((1 - t1) * xA + t1 * xB);
            double z1 = (1 - t1) * zA + t1 * zB;
            Col col1 = a.getColor().mul(1 - t1).add(b.getColor().mul(t1));

            // V2
            double t2 = (y - yA) / (double) (yC - yA);
            int x2 = (int) Math.round((1 - t2) * xA + t2 * xC);
            double z2 = (1 - t2) * zA + t2 * zC;
            Col col2 = a.getColor().mul(1 - t2).add(c.getColor().mul(t2));


            // TODO: kontrola, jestli x1 < x2

                if(x1 > x2)
                {
                    int minX = Math.min(x2, zBuffer.getWidth());
                    int maxX = Math.max(x1, 0);
                    for (int x = maxX; x >= minX; x--) {
                        double t3 = (x - x1) / (double) (x2 - x1);
                        double z = (1 - t3) * z1 + t3 * z2;
                        Col col = col1.mul(1 - t3).add(col2.mul(t3));

                        zBuffer.setPixelWithZTest(x, y, z, col);
                    }
                }
                else {
                    int minX = Math.min(x2, zBuffer.getWidth());
                    int maxX = Math.max(x1, 0);
                    for (int x = maxX; x <= minX; x++) {
                        double t3 = (x - x1) / (double) (x2 - x1);
                        double z = (1 - t3) * z1 + t3 * z2;
                        Col col = col1.mul(1 - t3).add(col2.mul(t3));

                        zBuffer.setPixelWithZTest(x, y, z, col);
                    }
                }
        }

        minY = Math.min(yC, zBuffer.getHeight());
        maxY = Math.max(yB, 0);
        for (int y = maxY; y <= minY; y++) {
            // V1
            double t1 = (y - yB) / (double) (yC - yB);
            int x1 = (int) Math.round((1 - t1) * xB + t1 * xC);
            double z1 = (1 - t1) * zB + t1 * zC;
            Col col1 = b.getColor().mul(1 - t1).add(c.getColor().mul(t1));

            // V2
            double t2 = (y - yA) / (double) (yC - yA);
            int x2 = (int) Math.round((1 - t2) * xA + t2 * xC);
            double z2 = (1 - t2) * zA + t2 * zC;
            Col col2 = a.getColor().mul(1 - t2).add(c.getColor().mul(t2));

            // TODO: kontrola, jestli x1 < x2
            if(x1 > x2)
            {
                int minX = Math.min(x2, zBuffer.getWidth());
                int maxX = Math.max(x1, 0);
                for (int x = maxX; x >= minX; x--) {
                    double t3 = (x - x1) / (double) (x2 - x1);
                    double z = (1 - t3) * z1 + t3 * z2;
                    Col col = col1.mul(1 - t3).add(col2.mul(t3));

                    zBuffer.setPixelWithZTest(x, y, z, col);
                }
            }
            else {
                int minX = Math.min(x2, zBuffer.getWidth());
                int maxX = Math.max(x1, 0);
                for (int x = maxX; x <= minX; x++) {
                    double t3 = (x - x1) / (double) (x2 - x1);
                    double z = (1 - t3) * z1 + t3 * z2;
                    Col col = col1.mul(1 - t3).add(col2.mul(t3));

                    zBuffer.setPixelWithZTest(x, y, z, col);
                }
            }
        }
    }
}
