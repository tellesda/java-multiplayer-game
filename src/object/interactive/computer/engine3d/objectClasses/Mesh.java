package object.interactive.computer.engine3d.objectClasses;



import object.interactive.computer.engine3d.mathClasses.*;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class Mesh extends Entity {

    //Attributes
    private String name;
    private Color material;
    private List<Triangle> triangles;
    private int numberOfTriangles;

    //Constructor
    public Mesh(String path, Vector3d location, Rotator3d rotation, Vector3d scale, Color material){
        this.material = material;
        this.location = location;
        this.rotation = rotation;
        this.scale = scale;
        loadMesh(path);
    }

    //Getters
    public String getName() { return name; }
    public List<Triangle> getTriangles() { return triangles; }
    public Color getMaterial() { return material; }

    //Setters
    public void setMaterial(Color material){
        this.material = material;
    }

    //Methods
    public void loadMesh(String path){

        triangles = new ArrayList<>();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();

            List<Vector3d> vertices = new ArrayList<>();
            List<Triangle> triangles = new ArrayList<>();

            while(line != null){

                char label = line.charAt(0);

                if(label == 'o'){
                    String[] parts = line.split(" ");
                    this.name = parts[1];
                }

                if(label == 'v'){
                    String[] parts = line.split(" ");
                    Vector3d vertex = new Vector3d(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]));
                    vertices.add(vertex);
                }
                else if(label == 'f'){
                    String[] parts = line.split(" ");
                    int index0 = Integer.parseInt(parts[1]) -1;
                    int index1 = Integer.parseInt(parts[2]) -1;
                    int index2 = Integer.parseInt(parts[3]) -1;
                    Triangle triangle = new Triangle(vertices.get(index0), vertices.get(index1), vertices.get(index2), material);
                    triangles.add(triangle);
                }

                line = reader.readLine();
            }
            reader.close();
            this.triangles = triangles;
            this.numberOfTriangles = triangles.size();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    //Init (Runs once)
    public void init(){

    }

    //Tick (Runs every frame)
    public void tick(){

    }

    //Render
    public List<Triangle> render(Matrix projection, Matrix camTransform, int width, int height, Camera camera){

        List<Triangle> trianglesToSort = new ArrayList<>(numberOfTriangles);

        Matrix matrixM = Matrix.getTransformMatrix(getLocation(), getRotation(), getScale());

        for(var triangle : triangles){

            triangle.verticesToCameraSpace(matrixM, camTransform);

            //Calculate normal
            triangle.setNormal(triangle.getCamVertex(0),
                                        triangle.getCamVertex(1),
                                        triangle.getCamVertex(2));

            //Stop if the face is not visible
            if(Vector3d.dotProduct(triangle.getNormal(), triangle.getCamVertex(0)) < 0){
                continue;
            }

            //Clipping
            List<Triangle> clippedTriangles = triangle.clip(camera.getClipPlanes(), camTransform);
            if(clippedTriangles == null){
                continue;
            }
            for(var clippedTriangle : clippedTriangles){
                clippedTriangle.projectVertices(projection, width, height);
                trianglesToSort.add(clippedTriangle);
            }

        }

       return trianglesToSort;


    }

}
