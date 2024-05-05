package raster;

import solid.Vertex;
import transforms.Col;

import java.awt.*;

public class LineRasterizer extends ObjectRasterizer{
    //private final ZBuffer zBuffer;
    public LineRasterizer(ZBuffer zBuffer)
    {
        super(zBuffer);
    }

    public void rasterize(Vertex a, Vertex b,Col color)
    {
        if (b.getPosition().getY() < a.getPosition().getY())
        {
            Vertex temp = new Vertex(b);
            b = new Vertex(a);
            a = new Vertex(temp);
        }

        int xA = (int) a.getPosition().getX();
        int yA = (int) a.getPosition().getY();
        int zA = (int) a.getW();
        int yB = (int) b.getPosition().getY();
        int xB = (int) b.getPosition().getX();
        int zB = (int) b.getW();

        float q;
        if(xA!=xB) //non vertical lines
        {
            float k = (yB-yA)/(float)(xB-xA);
            q = yB - k*xA;

            if(k<1&&k>-1) { // y = kx + q
                if(xA<xB)for(int i = xA; i <= xB; i++)
                {
                    double t1 = (i - xA) / (double) (xB - xA);
                    int y1 = (int) Math.round((1 - t1) * yA + t1 * yB);
                    double z1 = (1 - t1) * zA + t1 * zB;
                    zBuffer.setPixelWithZTest(i,y1,z1,color);
                    //zBuffer.setPixelWithZTest(i,(int)(k*i+q),0.2,color);
                }
                else for(int i = xB; i <= xA; i++){
                    double t1 = (i - xA) / (double) (xB - xA);
                    int y1 = (int) Math.round((1 - t1) * yA + t1 * yB);
                    double z1 = (1 - t1) * zA + t1 * zB;
                    zBuffer.setPixelWithZTest(i,y1,z1,color);
                    //zBuffer.setPixelWithZTest(i,(int)(k*i+q),0.2,color);
                }
            }

            else { // x = y/k -q/k
                if(yA>yB)for(int i = yB; i <= yA; i++)
                {
                    double t1 = (i - yA) / (double) (yB - yA);
                    int x1 = (int) Math.round((1 - t1) * xA + t1 * xB);
                    double z1 = (1 - t1) * zA + t1 * zB;
                    zBuffer.setPixelWithZTest(x1,i,z1,color);
                    //zBuffer.setPixelWithZTest((int)(i/k - q/k),i,0.2, color);
                }
                else for(int i = yA; i <= yB; i++)
                {
                    double t1 = (i - yA) / (double) (yB - yA);
                    int x1 = (int) Math.round((1 - t1) * xA + t1 * xB);
                    double z1 = (1 - t1) * zA + t1 * zB;
                    zBuffer.setPixelWithZTest(x1,i,z1,color);
                    //zBuffer.setPixelWithZTest((int)(i/k - q/k),i,0.2,color);
                }
            }
        }
        else //vertical line
        {
            if(yA>yB) for(int i = yB; i<= yA; i++)
            {
                double t1 = (i - yA) / (double) (yB - yA);
                int x1 = (int) Math.round((1 - t1) * xA + t1 * xB);
                double z1 = (1 - t1) * zA + t1 * zB;
                zBuffer.setPixelWithZTest(x1,i,z1,color);
                //zBuffer.setPixelWithZTest(xA,i,0.2,color);
            }
            else for(int i = yA; i<= yB; i++)
            {
                double t1 = (i - yA) / (double) (yB - yA);
                int x1 = (int) Math.round((1 - t1) * xA + t1 * xB);
                double z1 = (1 - t1) * zA + t1 * zB;
                zBuffer.setPixelWithZTest(x1,i,z1,color);
                //zBuffer.setPixelWithZTest(xA,i,0.2,color);
            }
        }
    }
    public void drawLine(int x1, int y1, int x2, int y2, Col color) {
        float q;
        if(x2<x1) //swap direction
        {
            int z = x1; //swap x
            x1 = x2;
            x2= z;

            z = y1; //swap y
            y1 = y2;
            y2= z;
        }
        if(x1!=x2) //non vertical lines
        {
            float k = (y2-y1)/(float)(x2-x1);
            q = y1 - k*x1;

            if(k<1&&k>-1) { // y = kx + q
                for(int i = x1; i <= x2; i++)
                {
                    zBuffer.setPixelWithZTest(i,(int)(k*i+q),0.2,color);
                }
            }

            else { // x = y/k -q/k
                if(y1>y2)for(int i = y2; i <= y1; i++)
                {
                    zBuffer.setPixelWithZTest((int)(i/k - q/k),i,0.2, color);
                }
                else for(int i = y1; i <= y2; i++)
                {
                    zBuffer.setPixelWithZTest((int)(i/k - q/k),i,0.2,color);
                }
            }
        }
        else //vertical line
        {
            if(y1>y2) for(int i = y2; i<= y1; i++)
            {
                zBuffer.setPixelWithZTest(x1,i,0.2,color);
            }
            else for(int i = y1; i<= y2; i++)
            {
                zBuffer.setPixelWithZTest(x1,i,0.2,color);
            }
        }
    }
}
