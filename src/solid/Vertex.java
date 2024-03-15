package solid;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec3D;

public class Vertex {
    private final Point3D position;
    private final Col color;
    private double w=0.5;

    // TODO: souřadnice do textury, normála atd.

    public static Vertex FromVec3D(Vec3D v)
    {
        Vertex ret = new Vertex(new Point3D(v.getX(),v.getY(),v.getZ()),new Col(0xffffff));
        return ret;
    }
    public static Vertex FromVec3D(Vec3D v,Col c)
    {
        Vertex ret = new Vertex(new Point3D(v.getX(),v.getY(),v.getZ()),c);
        return ret;
    }
    public static Vertex FromVec3D(Vec3D v,Col c,double w)
    {
        Vertex ret = new Vertex(new Point3D(v.getX(),v.getY(),v.getZ()),c,w);
        return ret;
    }

    public Vertex(Point3D position, Col color) {
        this.position = position;
        this.color = color;
    }
    public Vertex(Point3D position, Col color,double w) {
        this.position = position;
        this.color = color;
        this.w = w;
    }
    public Vertex(Vertex newVertex)
    {
        this.position = newVertex.getPosition();
        this.color = newVertex.getColor();
        this.w = newVertex.getW();
    }

    public Point3D getPosition() {
        return position;
    }

    public Col getColor() {
        return color;
    }
    public double getW(){return w;}

    public Vertex mul(final double x)
    {
        Point3D newPoint = this.position.mul(x);
        Col newCol = this.color.mul(x);
        return new Vertex(newPoint,newCol);
    }
    public Vertex add(Vertex v)
    {
        Point3D newPoint = this.position.add(v.getPosition());
        Col newCol = this.color.add(v.getColor());
        return new Vertex(newPoint,newCol);
    }
}
