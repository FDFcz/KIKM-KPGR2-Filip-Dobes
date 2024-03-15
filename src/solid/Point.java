package solid;

import transforms.Point3D;

import java.awt.*;

public class Point extends Solid{ //Shows point
    public Point()
    {
        vb.add(new Point3D(1, 0, 0)); // v0
        vb.add(new Point3D(-1, 0, 0)); // x
        vb.add(new Point3D(0, 1, 0)); // y
        vb.add(new Point3D(0, -1, 0)); // z
        vb.add(new Point3D(0, 0, 1)); // z
        vb.add(new Point3D(0, 0, -1)); // z

        addIndicesEdges(0, 1, 2, 3, 4, 5);
    }
    @Override
    public Color getColor(int index) {
        if (index == 0) return Color.RED;
        if (index == 1) return Color.GREEN;
        return Color.blue;
    }
}
