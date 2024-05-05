package solid;

import transforms.Point3D;

import java.awt.*;

public class Arrow extends Solid {
    public Arrow() {
        // vb
        vb.add(new Point3D(0, 0, 0)); // v0

        vb.add(new Point3D(0.5, 0, 0)); // v1
        vb.add(new Point3D(0, 0.5, 0)); // v2
        vb.add(new Point3D(0, 0, 0.5)); // v3
        //x
        vb.add(new Point3D(0.5, 0.1, 0)); // v4
        vb.add(new Point3D(0.5, -0.1, 0)); // v5
        vb.add(new Point3D(0.8, 0, 0)); // v6
        //z
        vb.add(new Point3D( 0,0.5,0.1)); // v7
        vb.add(new Point3D(  0,0.5,-0.1)); // v8
        vb.add(new Point3D(  0,0.8,0)); // v9
        //y
        vb.add(new Point3D(0.1, 0,0.5)); // v10
        vb.add(new Point3D( -0.1, 0,0.5)); // v11
        vb.add(new Point3D( 0, 0,0.8)); // v12


        // ib
        addIndicesEdges(
                0,1,
                0,2,
                0,3,

                4,5,6,
                7,8,9,
                10,11,12
        );
        partBuffer.add( new Part(TopologyType.LINES,0,3));
        partBuffer.add(new Part(TopologyType.TRIANGLES, 6, 3));
    }
    @Override
    public Color getColor(int index) {
        switch (index)
        {
            case 0:
                return Color.red;
            case 1:
                return Color.green;
            case 2:
                return Color.blue;
            default:
                return Color.magenta;
        }
    }
}
