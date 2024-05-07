package control;

import raster.Raster;
import raster.TriangleRasterizer;
import raster.ZBuffer;
import renderer.Renderer;
import solid.*;
import solid.Point;
import transforms.*;
import view.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Controller3D implements Controller {

    private final Panel panel;

    //private LineRasterizer rasterizer;

    private Camera camera;
    private Mat4 projOrtogonal;
    private Mat4 projPrespective;

    private Boolean isOrtogonal = false;
    private  Boolean is3Dscene=false;

    float mouseX,mouseY;
    //float lastDeltaTime;

    Solid cube = new Cube();
    Solid pyramid = new Pyramid();
    Solid pointZero = new Point();
    Solid house = new House();
    Solid plane = new Plane();
    Solid arrow = new Arrow();

    Solid[] solids = new Solid [3];
    int selectedSolidID =0;

    private ZBuffer zBuffer;
    private TriangleRasterizer triangleRasterizer;
    private Renderer render;

    Vertex testVertex = new Vertex(new Point3D(799, 359, 0.5), new Col(0x0000ff));

    public Controller3D(Panel panel) {
        this.panel = panel;
        initObjects(panel.getRaster());
        initListeners();
        redraw();
    }

    public void initObjects(Raster<Col> raster) {
        raster.setDefaultValue(new Col(0x101010));

        zBuffer = new ZBuffer(raster);
        triangleRasterizer = new TriangleRasterizer(zBuffer);
        render = new Renderer(zBuffer);
        solids[0] = house;
        solids[1] = pyramid;
        solids[2] = cube;

        arrow.setPos(new Mat4Transl(0,0,0));
        //arrow.setRot(new Mat4RotXYZ(1,0,1));
        arrow.setScale(new Mat4Scale(3,3,3));

        cube.setPos(new Mat4Transl(0,-10,0));
        cube.setRot(new Mat4RotXYZ(0,0,2));
        cube.setScale(new Mat4Scale(5,5,5));

        plane.setPos(new Mat4Transl(10,0,-8));
        plane.setRot(new Mat4RotXYZ(0,0,0));
        plane.setScale(new Mat4Scale(60,60,60));

        house.setPos(new Mat4Transl(0,12,0));
        house.setRot(new Mat4Rot(1,0,new Vec3D(1,0,0)));
        house.addRot(new Mat4Rot(1,0,new Vec3D(0,0,1)));
        house.setScale(new Mat4Scale(15,15,15));
        house.setColor(Color.BLUE);

        pyramid.setPos(new Mat4Transl(70,0,10));
        pyramid.setRot(new Mat4RotXYZ(1,0,1));
        pyramid.setScale(new Mat4Scale(10,10,10));


        pointZero.setScale(new Mat4Scale(2,2,2));

        camera = new Camera(
                new Vec3D(0, -1, 0.3),
                Math.toRadians(90),
                Math.toRadians(-15),
                1,
                true
        );
        camera = new Camera();

        projPrespective = new Mat4PerspRH(
                Math.PI / 4,
                (double) zBuffer.getHeight() / (double)zBuffer.getWidth(),
                0.1,
                200
        );
        projOrtogonal = new Mat4OrthoRH(80,60,0.1,200);


        camera = camera.backward(53);
        //camera = camera.right(26);
        //camera = camera.down(12);
        render.setProj(projPrespective);

        redraw();
    }

    @Override
    public void initListeners() {
        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                float w = e.getScrollAmount();
                cube.addScale(new Mat4Scale(w,w,w));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isControlDown()) return;

                if (e.isShiftDown()) {
                    //TODO
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    // rasterizer.rasterize(x, y, e.getX(),e.getY(), Color.RED);
                } else if (SwingUtilities.isMiddleMouseButton(e)) {
                    //TODO
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    //TODO
                }

                mouseX = e.getX();
                mouseY = e.getY();
                testVertex = new Vertex(new Point3D(mouseX, mouseY, 0.5), new Col(0x0000ff));
                redraw();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                testVertex = new Vertex(new Point3D(mouseX, mouseY, 0.5), new Col(0x0000ff));
                redraw();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isControlDown()) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        //TODO
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        //TODO
                    }
                }
            }
        });
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.isControlDown() || !is3Dscene) return;
                float deltaX = (e.getX()-mouseX)/50;
                float deltaY = (e.getY()-mouseY)/50;
                if (e.isShiftDown()) {
                    //TODO
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    camera = camera.addAzimuth(-deltaX);
                    camera = camera.addZenith(-deltaY);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    solids[selectedSolidID].addRot(new Mat4RotXYZ(deltaY,0,deltaX));
                } else if (SwingUtilities.isMiddleMouseButton(e)) {
                    //TODO
                }
                mouseX = e.getX();
                mouseY = e.getY();
                //update();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // na klávesu BACKSPACE vymazat plátno
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    //hardClear();
                }
                //Camera movement
                else if (e.getKeyCode() == KeyEvent.VK_A) {
                    camera = camera.left(0.5);
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    camera = camera.right(0.5);
                }else if (e.getKeyCode() == KeyEvent.VK_W) {
                    camera = camera.forward(0.5);
                } else if (e.getKeyCode() == KeyEvent.VK_S) {
                    camera = camera.backward(0.5);
                } else if (e.getKeyCode() == KeyEvent.VK_C) {
                    camera = camera.move(new Vec3D(0,0,-0.5));
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    camera = camera.move(new Vec3D(0,0,0.5));
                }

                //Solids movement
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    solids[selectedSolidID].addScale(new Mat4Scale(1.2,1.2,1.2));
                } else if (e.getKeyCode() == KeyEvent.VK_M) {
                    solids[selectedSolidID].addScale(new Mat4Scale(0.8,0.8,0.8));
                }else if (e.getKeyCode() == KeyEvent.VK_I) {
                    solids[selectedSolidID].addPos(new Mat4Transl(0,0,0.2));
                }else if (e.getKeyCode() == KeyEvent.VK_K) {
                    solids[selectedSolidID].addPos(new Mat4Transl(0,0,-0.2));
                } else if (e.getKeyCode() == KeyEvent.VK_J) {
                    solids[selectedSolidID].addPos(new Mat4Transl(-0.2,0,0));
                } else if (e.getKeyCode() == KeyEvent.VK_L) {
                    solids[selectedSolidID].addPos(new Mat4Transl(0.2,0,0));
                } else if (e.getKeyCode() == KeyEvent.VK_O) {
                    solids[selectedSolidID].addPos(new Mat4Transl(0,0.2,0));
                } else if (e.getKeyCode() == KeyEvent.VK_U) {
                    solids[selectedSolidID].addPos(new Mat4Transl(0,-0.2,0));
                } else if (e.getKeyCode() == KeyEvent.VK_E) {
                    solids[selectedSolidID].addRot(new Mat4RotXYZ(0,-0.1,0));
                } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                    solids[selectedSolidID].addRot(new Mat4RotXYZ(0,0.1,0));
                }

                //Switch active solid
                if (e.getKeyCode() == KeyEvent.VK_1)
                {
                    solids[selectedSolidID].setColor(Color.WHITE);
                    selectedSolidID =0;
                    solids[0].setColor(Color.BLUE);
                } else if (e.getKeyCode() == KeyEvent.VK_2) {
                    solids[selectedSolidID].setColor(Color.WHITE);
                    selectedSolidID =1;
                    solids[1].setColor(Color.BLUE);
                }  else if (e.getKeyCode() == KeyEvent.VK_3) {
                    solids[selectedSolidID].setColor(Color.WHITE);
                    selectedSolidID =2;
                    solids[2].setColor(Color.BLUE);
                }
                else if (e.getKeyCode() == KeyEvent.VK_0) {
                    is3Dscene = !is3Dscene;
                    if(!is3Dscene) redraw();
                }
                else if (e.getKeyCode() == KeyEvent.VK_9) {
                    isOrtogonal = !isOrtogonal;
                    if (isOrtogonal) render.setProj(projOrtogonal);
                    else render.setProj(projPrespective);
                }
                //update();
            }
        });

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.resize();
                initObjects(panel.getRaster());
            }
        });
    }
    public void update(float deltaTime)
    {
        if (selectedSolidID != 1) pyramid.addRot(new Mat4RotXYZ(deltaTime, 0, 0));
        if (selectedSolidID != 2) cube.addRot(new Mat4RotXYZ(deltaTime, 0, 0));

        //lastDeltaTime = deltaTime;
        update();
    }
    private void update() {
        if(!is3Dscene)return;
        panel.clear();

        //render.setProj(projPrespective);
        render.setView(camera.getViewMatrix());


        render.render(cube);
        render.render(pyramid);
        render.render(house);
        render.render(arrow);
        //render.render(plane);

        //render.render(pointZero);
        //show Camera cordinations
        //rasterizer.rasterize(2,2,40,2,Color.RED); //camera compass X R
        //rasterizer.rasterize(2,2,2,40,Color.GREEN); //camera compass Y G
        //rasterizer.rasterize(2,2,20,20,Color.BLUE); //camera compass Z B
        zBuffer.setDefaultValues();
        panel.repaint();
    }

    private void redraw() {
        panel.clear();

//        zBuffer.setPixelWithZTest(10, 10, 0.5, new Col(0xff0000));
//        zBuffer.setPixelWithZTest(10, 10, 0.7, new Col(0x00ff00));

        triangleRasterizer.rasterize(
                new Vertex(new Point3D(300, 270, 0.5), new Col(0xff0000)),
                new Vertex(new Point3D(120, 310, 0.5), new Col(0x00ff00)),
                testVertex
        );
        zBuffer.setDefaultValues();
        panel.repaint();
    }
}
