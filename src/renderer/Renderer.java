package renderer;

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


    public Renderer(ZBuffer raster)
    {
        this.raster =  raster;
        triangleRasterizer = new TriangleRasterizer(raster);
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
        for (Part pt : solid.getPartBuffer()) {
            switch (pt.getType())
            {
                case LINES:
                    break;
                case TRIANGLES:
                    int start = pt.getStart();
                    for(int n = 0; n < pt.getCount(); n++) {
                        int indexA = solid.getSb().get(n*3+start);
                        int indexB = solid.getSb().get(n*3+start+1);
                        int indexC = solid.getSb().get(n*3+start+2);

                        Point3D a = vb.get(indexA);
                        Point3D b = vb.get(indexB);
                        Point3D c = vb.get(indexC);

                        Col color = new Col(solid.getColor(n).getRGB()); //todo color
                        renderTriangle(a,b,c,color);
                    }
                    return;
                    //break;
            }
        }
        /*
        //vytvareni trojuhelniku
        for (int i = 0; i < solid.getSb().size(); i += 3) {
            int indexA = solid.getSb().get(i);
            int indexB = solid.getSb().get(i + 1);
            int indexC = solid.getSb().get(i + 2);

            Point3D a = vb.get(indexA);
            Point3D b = vb.get(indexB);
            Point3D c = vb.get(indexC);

            Col color = new Col(solid.getColor(i / 3).getRGB());
            renderTriangle(a,b,c,color);
        }
         */
    }
    protected void renderTriangle(Point3D a, Point3D b, Point3D c, Col color) {

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
        //if(zA<0 &;;+& zB<0 && zC<0)return; //??? je nutná


        // TODO: dehomogenizace
        double w = a.getW();
        Vec3D v1 = dehomogVertex(a);
        Vec3D v2 = dehomogVertex(b);
        Vec3D v3 = dehomogVertex(c);

        //todo seradit a-b-c

        // TODO: ořezání
        if (a.getZ() < 0) return;
        else if (b.getZ() < 0)
        {
            //TODO
            return;
        }
        else if (c.getZ() < 0)
        {

            //todo
            return;
        }
        else
        {
            v1 = trasformToViePort(v1);
            v2 = trasformToViePort(v2);
            v3 = trasformToViePort(v3);
            triangleRasterizer.rasterize(Vertex.FromVec3D(v1,color,a.getW()),Vertex.FromVec3D(v2,color,b.getW()),Vertex.FromVec3D(v3,color,c.getW()));
            return;
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
