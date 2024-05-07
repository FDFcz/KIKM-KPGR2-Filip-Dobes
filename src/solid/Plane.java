package solid;

import transforms.Point3D;

import java.awt.*;

public class Plane extends Solid {

    public Plane() {
        // vb
        vb.add(new Point3D(-0.5, -0.5,0)); // v0
        vb.add(new Point3D(0.5,  -0.5,0)); // v1
        vb.add(new Point3D(0.5,  0.5,0)); // v2
        vb.add(new Point3D(-0.5,  0.5,0)); // v3
        // ib
        addIndicesEdges(

                0,2,1,
                0,3,2
        );
        partBuffer.add(new Part(TopologyType.TRIANGLES,0,2));
    }
    @Override
    public Color getColor(int index) {
        return Color.pink;
    }
}
