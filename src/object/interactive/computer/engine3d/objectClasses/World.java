package object.interactive.computer.engine3d.objectClasses;

import java.awt.Graphics;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import engine.Engine;
import object.interactive.computer.engine3d.mathClasses.*;

public class World {

    //Attributes
    private String name;
    private Camera viewCam;
    private Sun sun;
    private List<Mesh> meshes;
    private Color skyColor;
    private final Engine parentEngine;

    //Constructor
    public World(String path, Engine engine){
        this.parentEngine = engine;
        loadWorld(path);
    }

    //Methods
    public void addMesh(String path, Vector3d location, Rotator3d rotation, Vector3d scale, Color material){
        meshes.add(new Mesh(path, location, rotation, scale, material));
    }

    //Init (Runs once)
    public void init(){

        //TODO DEBUG HERE


        viewCam.init();
        sun.init();
        for(var mesh : meshes){
            mesh.init();
        }
    }

    //Tick (Runs every frame)
    public void tick(){
        viewCam.tick();
        for(var mesh : meshes){
            mesh.tick();
        }
        //TODO DEBUG HERE
    }


    //Render
    public void render(Graphics g, int width, int height){

        Matrix projectionMatrix = Matrix.getProjectionMatrix(viewCam.getVerticalFov(),
                                                             viewCam.getVerticalFov(),
                                                             viewCam.getzFar(),
                                                             viewCam.getzNear());

        Matrix camTransform = Matrix.getTransformMatrix(viewCam.getLocation(),
                                                        viewCam.getRotation(),
                                                        viewCam.getScale());

        Matrix camTransformInverted = Matrix.invert(camTransform);

        g.setColor(skyColor);
        g.fillRect(0,0,width,height);

        List<Triangle> worldTriangles = new ArrayList<>(1000);

        for(var mesh : meshes){
            worldTriangles.addAll(mesh.render(projectionMatrix, camTransformInverted, width, height, viewCam));
        }

        worldTriangles.sort(Comparator.comparing(tri -> tri.getDepth()* -1f));
        sun.setLightDirection(camTransformInverted);
        for(var triangle : worldTriangles){
            triangle.drawTriangle(g, sun);
            //triangle.drawWireFrame(g, Color.black);
        }
    }

    public void loadWorld(String path){

        meshes = new ArrayList<>();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();

            while(line != null){

                char label = line.charAt(0);

                if(label == 'n'){
                    String[] parts = line.split(" ");
                    this.name = parts[1];
                }

                if(label == 'l'){
                    String[] parts = line.split(" ");
                    Rotator3d sunRotation = new Rotator3d(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]));
                    int sunR = Integer.parseInt(parts[4]);
                    int sunG = Integer.parseInt(parts[5]);
                    int sunB = Integer.parseInt(parts[6]);
                    float ambientLight = Float.parseFloat(parts[7]);
                    this.sun = new Sun(sunRotation, new Color(sunR, sunG, sunB), ambientLight);

                }
                if(label == 's'){
                    String[] parts = line.split(" ");
                    int skyR = Integer.parseInt(parts[1]);
                    int skyG = Integer.parseInt(parts[2]);
                    int skyB = Integer.parseInt(parts[3]);
                    int skyA = Integer.parseInt(parts[4]);
                    this.skyColor = new Color(skyR, skyG, skyB, skyA);
                }
                if(label == 'm'){
                    String[] parts = line.split(" ");
                    String meshPath = parts[1];
                    Vector3d loc = new Vector3d(Float.parseFloat(parts[2]), Float.parseFloat(parts[3]), Float.parseFloat(parts[4]));
                    Rotator3d rot = new Rotator3d(Float.parseFloat(parts[5]), Float.parseFloat(parts[6]), Float.parseFloat(parts[7]));
                    Vector3d scale = new Vector3d(Float.parseFloat(parts[8]), Float.parseFloat(parts[9]), Float.parseFloat(parts[10]));
                    int meshR = Integer.parseInt(parts[11]);
                    int meshG = Integer.parseInt(parts[12]);
                    int meshB = Integer.parseInt(parts[13]);
                    addMesh(meshPath, loc, rot, scale, new Color(meshR, meshG, meshB));
                }
                else if(label == 'v'){
                    String[] parts = line.split(" ");
                    Vector3d loc = new Vector3d(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]));
                    Rotator3d rot = new Rotator3d(Float.parseFloat(parts[4]), Float.parseFloat(parts[5]), Float.parseFloat(parts[6]));
                    Vector3d scale = new Vector3d(Float.parseFloat(parts[7]), Float.parseFloat(parts[8]), Float.parseFloat(parts[9]));

                    viewCam = new Camera(this.parentEngine, loc, rot, scale);
                }

                line = reader.readLine();
            }
            reader.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
