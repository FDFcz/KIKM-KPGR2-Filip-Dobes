package solid;

import transforms.Point3D;

import java.awt.*;

public class House extends Solid {
    public House()
    {

        //Cube
        vb.add(new Point3D(-0.5, -0.5, -0.5)); // v0
        vb.add(new Point3D(0.5, -0.5, -0.5)); // v1
        vb.add(new Point3D(0.5, 0.5, -0.5)); // v2
        vb.add(new Point3D(-0.5, 0.5, -0.5)); // v3
        vb.add(new Point3D(-0.5, -0.5, 0.5)); // v4
        vb.add(new Point3D(0.5, -0.5, 0.5)); // v5
        vb.add(new Point3D(0.5, 0.5, 0.5)); // v6
        vb.add(new Point3D(-0.5, 0.5, 0.5)); // v7
        //Door
        vb.add(new Point3D(-0.25, -0.5, -0.5)); // v8
        vb.add(new Point3D(0.25, -0.5, -0.5)); // v9
        vb.add(new Point3D(0.25, 0.2, -0.5)); // v10
        vb.add(new Point3D(-0.25, 0.2, -0.5)); // v11
        //roof
        vb.add(new Point3D(0, 1, -0.5)); // v12
        vb.add(new Point3D(0, 1, 0.5)); // v13

        // ib
        addIndicesEdges(
                0, 1,
                1, 2,
                2, 3,
                3, 0,

                4, 5,
                5, 6,
                6, 7,
                7, 4,

                7, 3,
                6, 2,
                5, 1,
                4, 0,

                8,11,
                9,10,
                10,11,

                3,12,
                2,12,
                12,13,
                6,13,
                7,13
        );
        addIndicatesSurfaces(
                0,8,11,
                11,0,3,
                3,11,10,
                10,3,2,
                2,10,1,
                10,9,1,

                0,4,7,
                0,7,3,

                1,5,6,
                1,6,2,

                0,1,5,
                0,5,4,

                4,5,6,
                4,6,7,

                3,12,2,
                6,13,7,
                2,12,13,
                2,13,6,
                3,12,13,
                3,13,7
        );

        addColors(
                Color.pink,
                Color.pink,
                Color.ORANGE,
                Color.ORANGE,
                Color.yellow,
                Color.yellow,
                Color.WHITE,
                Color.WHITE,
                Color.GRAY,
                Color.GRAY,
                Color.blue,
                Color.BLUE,
                Color.RED,
                Color.red,
                Color.MAGENTA,
                Color.MAGENTA,
                Color.MAGENTA,
                Color.MAGENTA
        );
        partBuffer.add(new Part(TopologyType.TRIANGLES,0,20));
    }
    @Override
    public Color getColor(int index) {
        switch (index)
        {
            case 0,1,2,3,4,5:
                return Color.yellow;
            case 6,7,8,9,12,13:
                return Color.orange;
            case 10,11:
                return Color.GRAY;
            case 16,17,18,19:
                return Color.RED;
            case 14,15:
                return Color.MAGENTA;
            default:
                return Color.white;
        }
    }
}
