package solid;

import transforms.Point3D;

import java.awt.*;

public class Pyramid extends Solid{
    public Pyramid()
    {
        //vb
        vb.add(new Point3D(-0.5, -0.5, -0.5)); // v0
        vb.add(new Point3D(0.5, -0.5, -0.5)); // v1
        vb.add(new Point3D(0.5, 0.5, -0.5)); // v2
        vb.add(new Point3D(-0.5, 0.5, -0.5)); // v3
        vb.add(new Point3D(0, 0, 0.5)); // v4

        // ib
        addIndicesEdges(
                0, 1,
                1, 2,
                2, 3,
                3, 0,

                0,4,
                1,4,
                2,4,
                3,4
        );

        addIndicatesSurfaces(
                0,2,1,
                        0,3,2,

                        0,4,1,
                        1,4,2,
                        2,4,3,
                        3,4,0
        );
        addColors(
                Color.RED,
                Color.RED,
                Color.GREEN,
                Color.BLUE,
                Color.MAGENTA,
                Color.CYAN
        );
    }
    @Override
    public Color getColor(int index) {
        switch (index)
        {
            case 0,1:
                return Color.RED;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.blue;
            case 4:
                return Color.MAGENTA;
            case 5:
                return Color.yellow;
            default:
                return Color.white;
        }
    }
}
