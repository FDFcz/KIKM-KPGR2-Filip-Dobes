package renderer;

import raster.Raster;
import raster.TriangleRasterizer;
import raster.ZBuffer;
import solid.Part;
import solid.Solid;
import solid.Vertex;
import transforms.Col;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;
import view.Panel;

import java.awt.*;
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
                    break;
            }
        }

        //vytvareni trojuhelniku
        for (int i = 0; i < solid.getSb().size(); i += 3) {
            int indexA = solid.getSb().get(i);
            int indexB = solid.getSb().get(i + 1);
            int indexC = solid.getSb().get(i + 2);

            Point3D a = vb.get(indexA);
            Point3D b = vb.get(indexB);
            Point3D c = vb.get(indexC);

            //rychle ořezání
            double wA = a.getW();
            double wB = a.getW();
            double wC = a.getW();
            double xA = a.getX();
            double xB = b.getX();
            double xC = c.getX();
            if(xA>wA && xB>wB && xC>wC)continue;
            if(xA<-wA && xB<-wB && xC<-wC)continue;
            double yA = a.getY();
            double yB = b.getY();
            double yC = c.getY();
            if(yA>wA && yB>wB && yC>wC)continue;
            if(yA<-wA && yB<-wB && yC<-wC)continue;
            double zA = a.getZ();
            double zB = b.getZ();
            double zC = c.getZ();
            if(zA>wA && zB>wB && zC>wC)continue;
            //if(zA<0 &;;+& zB<0 && zC<0)continue; //??? je nutná

            Col color = new Col(solid.getColor(i / 3).getRGB());
            _render(a,b,c,color);
        }
    }
    protected void renderTriangel(Point3D a, Point3D b, Point3D c, Col color)
    {

    }
    protected void _render(Point3D a, Point3D b, Point3D c, Col color) {

        // TODO: dehomogenizace
        double w = a.getW();
        Vec3D v1 = new Vec3D();
        Vec3D v2 = new Vec3D();
        Vec3D v3 = new Vec3D();
        if (a.dehomog().isPresent()) v1 = a.dehomog().get();
        if (b.dehomog().isPresent()) v2 = b.dehomog().get();
        if (c.dehomog().isPresent()) v3 = c.dehomog().get();


        //todo seradit a-b-c

        // TODO: ořezání
        if (a.getZ() < 0) return;
        else if (b.getZ() < 0)
        {

        }
        else if (c.getZ() < 0)
        {

        }
        else
        {
            v1 = trasformToViePort(v1);
            v2 = trasformToViePort(v2);
            v3 = trasformToViePort(v3);
            triangleRasterizer.rasterize(Vertex.FromVec3D(v1,color,a.getW()),Vertex.FromVec3D(v2,color,b.getW()),Vertex.FromVec3D(v3,color,c.getW()));
            return;
        }

        // TODO: tranformace do okna obrazovky
        v1 = v1.mul(new Vec3D(1,-1,1));
        v2 = v2.mul(new Vec3D(1,-1,1));
        v3 = v3.mul(new Vec3D(1,-1,1));

        v1 = v1.add(new Vec3D(1,1,0));
        v2 = v2.add(new Vec3D(1,1,0));
        v3 = v3.add(new Vec3D(1,1,0));

        v1 = v1.mul(new Vec3D((raster.getWidth()-1)/2, (raster.getHeight()-1)/2,1));
        v2 = v2.mul(new Vec3D((raster.getWidth()-1)/2, (raster.getHeight()-1)/2,1));
        v3 = v3.mul(new Vec3D((raster.getWidth()-1)/2, (raster.getHeight()-1)/2,1));


        triangleRasterizer.rasterize(Vertex.FromVec3D(v1,color,a.getW()),Vertex.FromVec3D(v2,color,b.getW()),Vertex.FromVec3D(v3,color,c.getW()));

    }
    Vec3D trasformToViePort(Vec3D v)
    {
        v = v.mul(new Vec3D(1,-1,1));
        v = v.add(new Vec3D(1,1,0));
        v = v.mul(new Vec3D((raster.getWidth()-1)/2, (raster.getHeight()-1)/2,1));
        return v;
    }
    Vec3D DehomogVecrtex(Point3D a)
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
