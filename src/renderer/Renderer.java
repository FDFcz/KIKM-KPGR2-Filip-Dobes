package renderer;

import raster.LineRasterizer;
import raster.TriangleRasterizer;
import raster.ZBuffer;
import solid.Part;
import solid.Solid;
import solid.Vertex;
import transforms.Col;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    protected Mat4 view, proj;
    //protected List<Point3D> vb;
    private ZBuffer raster;
    TriangleRasterizer triangleRasterizer;
    LineRasterizer lineRasterizer;


    public Renderer(ZBuffer raster)
    {
        this.raster =  raster;
        triangleRasterizer = new TriangleRasterizer(raster);
        lineRasterizer = new LineRasterizer(raster);
    }
    public void render(Solid solid)
    {
        //point transform, I tranform every Point only once
        List<Point3D> vb = new ArrayList<>();
        List<Point3D> temp_VB = solid.getVb();
        for (int i = 0;i<temp_VB.size();i++) {
            Point3D a = temp_VB.get(i);
            // modelovací tranformace
            a = a.mul(solid.getModel());
            // pohledová tranformace
            a = a.mul(view);
            // projekční tranformace
            a = a.mul(proj);
            vb.add(new Point3D(a));
        }

        //todo partbuffer
        int start;
        for (Part pt : solid.getPartBuffer()) {
            switch (pt.getType())
            {
                case LINES:
                    start = pt.getStart();
                    for(int n = 0; n < pt.getCount(); n++) {
                        int indexA = solid.getIb().get(n*2+start);
                        int indexB = solid.getIb().get(n*2+start+1);

                        Point3D a = vb.get(indexA);
                        Point3D b = vb.get(indexB);

                        Col color = new Col(solid.getColor(n).getRGB()); //todo color
                        renderLine(a,b,color);
                    }
                    break;
                case TRIANGLES:
                    start = pt.getStart();
                    for(int n = 0; n < pt.getCount(); n++) {
                        int indexA = solid.getIb().get(n*3+start);
                        int indexB = solid.getIb().get(n*3+start+1);
                        int indexC = solid.getIb().get(n*3+start+2);

                        Point3D a = vb.get(indexA);
                        Point3D b = vb.get(indexB);
                        Point3D c = vb.get(indexC);

                        Col color = new Col(solid.getColor(n).getRGB()); //todo color
                        renderTriangle(a,b,c,color);
                    }
                    break;
            }
        }
    }

    protected void renderLine(Point3D a, Point3D b, Col color) {

        if (b.getZ() > a.getZ())
        {
            Point3D temp= b;
            b = a;
            a = temp;
        }

        //rychle ořezání
        double wA = a.getW();
        double wB = a.getW();
        double wC = a.getW();
        double xA = a.getX();
        double xB = b.getX();
        if(xA>wA && xB>wB)return;
        if(xA<-wA && xB<-wB)return;
        double yA = a.getY();
        double yB = b.getY();
        if(yA>wA && yB>wB)return;
        if(yA<-wA && yB<-wB)return;
        double zA = a.getZ();
        double zB = b.getZ();
        if(zA>wA && zB>wB)return;
        if(zA<0)return;


        // TODO: dehomogenizace
        double w = a.getW();
        Vec3D v1 = dehomogVertex(a);
        Vec3D v2 = dehomogVertex(b);


        // TODO: ořezání
        if (b.getZ() < 0)
        {
            double t1 = (0 - zA) / (double) (zB - zA);
            double x = (1 - t1) * xA + t1 * xB;
            double y = (1 - t1) * yA + t1 * yB;
            double z = (1 - t1) * zA + t1 * zB;
            double w2 = (1 - t1) * wA + t1 * wB;
            v2 = new Vec3D(new Point3D(x,y,z));

            v1 = trasformToViePort(v1);
            v2 = trasformToViePort(v2);
            lineRasterizer.rasterize(Vertex.FromVec3D(v1,color,a.getW()),Vertex.FromVec3D(v2,color,w2),color);
        }
        else
        {
            v1 = trasformToViePort(v1);
            v2 = trasformToViePort(v2);
            lineRasterizer.rasterize(Vertex.FromVec3D(v1,color,a.getW()),Vertex.FromVec3D(v2,color,b.getW()),color);
        }
    }
    protected void renderTriangle(Point3D a, Point3D b, Point3D c, Col color) {

        //todo seradit a-b-c
        if (c.getZ() > b.getZ())
        {
            Point3D temp= c;
            c = b;
            b = temp;
        }
        if (b.getZ() > a.getZ())
        {
            Point3D temp= b;
            b = a;
            a = temp;
        }
        if (c.getZ() > b.getZ())
        {
            Point3D temp= c;
            c = b;
            b = temp;
        }
        //rychle ořezání
        double wA = a.getW();
        double wB = a.getW();
        double wC = a.getW();
        double xA = a.getX();
        double xB = b.getX();
        double xC = c.getX();
        if(xA>wA && xB>wB && xC>wC)return;
        if(xA<-wA && xB<-wB && xC<-wC)return;
        double yA = a.getY();
        double yB = b.getY();
        double yC = c.getY();
        if(yA>wA && yB>wB && yC>wC)return;
        if(yA<-wA && yB<-wB && yC<-wC)return;
        double zA = a.getZ();
        double zB = b.getZ();
        double zC = c.getZ();
        if(zA>wA && zB>wB && zC>wC)return;
        if(zA<0)return;


        // TODO: dehomogenizace
        //double w = a.getW();
        Vec3D v1 = dehomogVertex(a);
        Vec3D v2 = dehomogVertex(b);
        Vec3D v3 = dehomogVertex(c);


        xA = v1.getX();
        xB = v2.getX();
        xC = v3.getX();
        yA = v1.getY();
        yB = v2.getY();
        yC = v3.getY();
        zA = v1.getZ();
        zB = v2.getZ();
        zC = v3.getZ();

        // TODO: ořezání
        //if (a.getZ() < 0) return; jiz vylouceno
        if (b.getZ() < 0)
        {
            double t1 = (0 - zA) / (double) (zB - zA);
            double x = (1 - t1) * xA + t1 * xB;
            double y = (1 - t1) * yA + t1 * yB;
            double z = (1 - t1) * zA + t1 * zB;
            double w2 = (1 - t1) * wA + t1 * wB;
            v2 = new Vec3D(new Point3D(x,y,z));

            t1 = (0 - zA) / (double) (zC - zA);
            x = (1 - t1) * xA + t1 * xC;
            y = (1 - t1) * yA + t1 * yC;
            z = (1 - t1) * zA + t1 * zC;
            double w3 = (1 - t1) * wA + t1 * wC;
            v3 = new Vec3D(new Point3D(x,y,z));

            v1 = trasformToViePort(v1);
            v2 = trasformToViePort(v2);
            v3 = trasformToViePort(v3);
            //System.out.println("h");
            triangleRasterizer.rasterize(Vertex.FromVec3D(v1,color,a.getW()),Vertex.FromVec3D(v2,color,w2),Vertex.FromVec3D(v3,color,w3));
        }
        else if (c.getZ() < 0)
        {
            //System.out.println("k");
            double t1 = (0 - zA) / (double) (zC - zA);
            double x = (1 - t1) * xA + t1 * xC;
            double y = (1 - t1) * yA + t1 * yC;
            double z = (1 - t1) * zA + t1 * zC;
            double w4 = (1 - t1) * wA + t1 * wC;
            Vec3D v4 = new Vec3D(new Point3D(x,y,z));

            t1 = (0 - zB) / (double) (zC - zB);
            x = (1 - t1) * xB + t1 * xC;
            y = (1 - t1) * yB + t1 * yC;
            z = (1 - t1) * zB + t1 * zC;
            double w3 = (1 - t1) * wB + t1 * wC;
            v3 = new Vec3D(new Point3D(x,y,z));

            v1 = trasformToViePort(v1);
            v2 = trasformToViePort(v2);
            v3 = trasformToViePort(v3);
            v4 = trasformToViePort(v4);
            triangleRasterizer.rasterize(Vertex.FromVec3D(v1,color,a.getW()),Vertex.FromVec3D(v2,color,b.getW()),Vertex.FromVec3D(v3,color,w3));
            triangleRasterizer.rasterize(Vertex.FromVec3D(v1,color,a.getW()),Vertex.FromVec3D(v4,color,w4),Vertex.FromVec3D(v3,color,w3));
        }
        else
        {
            v1 = trasformToViePort(v1);
            v2 = trasformToViePort(v2);
            v3 = trasformToViePort(v3);
            triangleRasterizer.rasterize(Vertex.FromVec3D(v1,color,a.getW()),Vertex.FromVec3D(v2,color,b.getW()),Vertex.FromVec3D(v3,color,c.getW()));
        }
    }
    Vec3D trasformToViePort(Vec3D v)
    {
        v = v.mul(new Vec3D(1,-1,1));
        v = v.add(new Vec3D(1,1,0));
        v = v.mul(new Vec3D((raster.getWidth()-1)/2, (raster.getHeight()-1)/2,1));
        return v;
    }
    Vec3D dehomogVertex(Point3D a)
    {
        Vec3D v = new Vec3D();
        if (a.dehomog().isPresent()) v = a.dehomog().get();
        return v;
    }
    public void setView(Mat4 view) {
        this.view = view;
    }

    public void setProj(Mat4 proj) {
        this.proj = proj;
    }
}
