package solid;

import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Solid {

    protected  final ArrayList<Part> partBuffer = new ArrayList<>();
    public ArrayList<Part> getPartBuffer() {
        return partBuffer;
    }

    //------------------------------------------------------------------------------
    protected ArrayList<Point3D> vb = new ArrayList<>();
    protected ArrayList<Integer> ib = new ArrayList<>();
    protected ArrayList<Integer> sb = new ArrayList<>();
    protected ArrayList<Color> sc = new  ArrayList<>();

    protected Color color = Color.WHITE;
    //protected ArrayList<Color> cb = new ArrayList<>();

    private Mat4 model = new Mat4Identity();
    private Mat4 pos = new Mat4Identity();
    private Mat4 rot = new Mat4Identity();
    private Mat4 scale = new Mat4Identity();


    protected void addIndicesEdges(Integer... indices) {
        ib.addAll(Arrays.asList(indices));
    }
    protected void addIndicatesSurfaces(Integer... indices) {
        sb.addAll(Arrays.asList(indices));
    }
    protected void addColors(Color... indicates)
    {
        sc.addAll(Arrays.stream(indicates).toList());
    }

    public ArrayList<Point3D> getVb() {
        return vb;
    }

    public ArrayList<Integer> getIb() {
        return ib;
    }

    public Mat4 getModel() {return model;}
    public void setModel(){
        model = rot.mul(scale.mul(pos));
    }
    public void setModel(Mat4 model){this.model = model;}
    public Color getColor(){return color;}
    public Color getColor(int index){return color;}
    public void setColor(Color color){this.color =color;}
    public void setPos(Mat4 pos) {
        this.pos = pos;
        setModel();
    }
    public void addPos(Mat4 pos){
        this.pos = this.pos.mul(pos);
        setModel();
    }
    public void setRot(Mat4 rot) {
        this.rot = rot;
        setModel();
    }
    public void addRot(Mat4 rot)
    {
        this.rot = this.rot.mul(rot);
        setModel();
    }
    public void setScale(Mat4 scale) {
        this.scale = scale;
        setModel();
    }
    public void addScale(Mat4 scale)
    {
        this.scale = this.scale.mul(scale);
        setModel();
    }


    public Mat4 getRot(){return rot;}
    public void reorient()
    {
        Mat4 m = getModel();
        for (Point3D p : getVb()) {
            p = p.mul(m);
        }
    }
}
