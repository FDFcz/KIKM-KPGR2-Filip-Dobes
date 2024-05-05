package solid;

import transforms.Point3D;

import java.awt.*;

public class Cube extends Solid {

    public Cube() {
        // vb
        vb.add(new Point3D(-0.5, -0.5, -0.5)); // v0
        vb.add(new Point3D(0.5, -0.5, -0.5)); // v1
        vb.add(new Point3D(0.5, 0.5, -0.5)); // v2
        vb.add(new Point3D(-0.5, 0.5, -0.5)); // v3
        vb.add(new Point3D(-0.5, -0.5, 0.5)); // v4
        vb.add(new Point3D(0.5, -0.5, 0.5)); // v5
        vb.add(new Point3D(0.5, 0.5, 0.5)); // v6
        vb.add(new Point3D(-0.5, 0.5, 0.5)); // v7

        // ib
        addIndicesEdges(
                0,2,1,
                0,3,2,

                2,3,7,
                2,7,6,

                0,4,7,
                0,7,3,

                1,5,6,
                1,6,2,

                0,1,5,
                0,5,4,

                4,5,6,
                4,6,7
        );
        addColors(
                Color.RED,
                Color.RED,
                Color.GREEN,
                Color.GREEN,
                Color.BLUE,
                Color.BLUE,
                Color.MAGENTA,
                Color.MAGENTA,
                Color.CYAN,
                Color.CYAN,
                Color.YELLOW,
                Color.YELLOW,
                Color.GRAY,
                Color.GRAY,
                Color.pink,
                Color.pink
        );

        partBuffer.add(new Part(TopologyType.TRIANGLES,0,12));
    }
    @Override
    public Color getColor(int index) {
        switch (index)
        {
            case 0,1:
                return Color.RED;
            case 2,3:
                return Color.GREEN;
            case 4,5:
                return Color.blue;
            case 6,7:
                return Color.MAGENTA;
            case 8,9:
                return Color.yellow;
            case 10,11:
                return Color.cyan;
            default:
                return Color.white;
        }
    }
}
