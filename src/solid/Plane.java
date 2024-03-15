package solid;

import transforms.Point3D;

import java.awt.*;

public class Plane extends Solid {

    public Plane() {
        // vb
        vb.add(new Point3D(-0.5, 0, -0.5)); // v0
        vb.add(new Point3D(0.5, 0, -0.5)); // v1
        vb.add(new Point3D(0.5, 0, 0.5)); // v2
        vb.add(new Point3D(-0.5, 0, 0.5)); // v3
        // ib
        addIndicesEdges(
                0, 1,
                1, 2,
                2, 3,
                3, 0
        );
        addIndicatesSurfaces(
                0,2,1,
                0,3,2
        );
    }
    @Override
    public Color getColor(int index) {
        return Color.pink;
    }
}
