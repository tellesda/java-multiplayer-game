package object.interactive.computer.engine3d.objectClasses;


import object.interactive.computer.engine3d.mathClasses.*;

import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Triangle{

    //Attributes
    private final Color triangleColor;

    private final Vector3d[] vertices;
    private Vector3d normal;

    private final Vector3d[] camVertices; //Camera space
    private final Vector3d[] worldVertices; //World space

    private final Vector3d[] remainingVertices; //World space
    private final Vector3d[] intersecVertices; //World space

    private final int[] xPoints;
    private final int[] yPoints;

    //Constructor
    public Triangle(Vector3d v1, Vector3d v2, Vector3d v3, Color material){

        triangleColor = material;

        vertices = new Vector3d[3];
        vertices[0] = v1;
        vertices[1] = v2;
        vertices[2] = v3;
        this.normal = new Vector3d(0f,0f,0f);

        camVertices = new Vector3d[3];
        worldVertices = new Vector3d[3];

        remainingVertices = new Vector3d[2];
        intersecVertices = new Vector3d[2];

        xPoints = new int[3];
        yPoints = new int[3];
    }


    public Vector3d getNormal() {
        return normal;
    }

    public void setNormal(Vector3d v1, Vector3d v2, Vector3d v3){
        Vector3d edge1 = Vector3d.sub(v3,v1);
        Vector3d edge2 = Vector3d.sub(v2,v1);
        this.normal = Vector3d.normalize(Vector3d.crossProduct(edge2,edge1));
    }

    public void setNormal(Vector3d normal){
        this.normal = normal;
    }


    public void verticesToCameraSpace(Matrix meshTransform, Matrix camTransform){
        for(int vertex=0; vertex<3; vertex++){
            worldVertices[vertex] = Matrix.fastMul(meshTransform, vertices[vertex]);
            camVertices[vertex] = Matrix.fastMul(camTransform, worldVertices[vertex]);
        }

    }

    public void setCamVertices(Matrix camTransform){
        for(int vertex=0; vertex<3; vertex++){
            camVertices[vertex] = Matrix.fastMul(camTransform, worldVertices[vertex]);
        }
    }

    public void projectVertices(Matrix projection, int width, int height){

        for(int vertex=0; vertex<3; vertex++){
            Matrix vClip = Matrix.fastMul2(projection, camVertices[vertex].toMatrix4x1());
            Vector3d vPerspective = Matrix.applyPerspective(vClip);

            //Sizing to screen
            vPerspective.setVector((vPerspective.getX()+1f) * width*0.5f,
                                   (vPerspective.getY()+1f) * height*0.5f,
                                       vPerspective.getZ());

            xPoints[vertex] = (int)vPerspective.getX();
            yPoints[vertex] = (int)vPerspective.getY();
        }
    }

    public List<Triangle> clip(Plane[] clipPlanes, Matrix camTransform){

        for(var clipPlane : clipPlanes){
            int outerVertices = countOuterVertices(clipPlane);

            //Triangle not visible
            if(outerVertices == 3){
                return null;
            }

            //Triangle partially visible
            if(outerVertices == 2){
                Triangle tri1 = new Triangle(intersecVertices[0], intersecVertices[1], remainingVertices[0], triangleColor);
                tri1.setNormal(getNormal());
                tri1.setWorldVertex(0, intersecVertices[0]);
                tri1.setWorldVertex(1, intersecVertices[1]);
                tri1.setWorldVertex(2, remainingVertices[0]);
                tri1.setCamVertices(camTransform);

                return tri1.clip(clipPlanes, camTransform);

            }
            if(outerVertices == 1){
                Triangle tri1 = new Triangle(intersecVertices[0], intersecVertices[1], remainingVertices[0], triangleColor);
                Triangle tri2 = new Triangle(intersecVertices[0], remainingVertices[1], remainingVertices[0], triangleColor);

                tri1.setNormal(getNormal());
                tri1.setWorldVertex(0, intersecVertices[0]);
                tri1.setWorldVertex(1, intersecVertices[1]);
                tri1.setWorldVertex(2, remainingVertices[1]);
                tri1.setCamVertices(camTransform);

                tri2.setNormal(getNormal());
                tri2.setWorldVertex(0, intersecVertices[0]);
                tri2.setWorldVertex(1, remainingVertices[1]);
                tri2.setWorldVertex(2, remainingVertices[0]);
                tri2.setCamVertices(camTransform);

                List<Triangle> result = new ArrayList<>();
                List<Triangle> subTri1 = tri1.clip(clipPlanes, camTransform);
                List<Triangle> subTri2 = tri2.clip(clipPlanes, camTransform);

                if(subTri1 != null){
                    result.addAll(subTri1);
                }
                if(subTri2 != null){
                    result.addAll(subTri2);
                }
                return result;
            }
        }

        List<Triangle> result = new ArrayList<>();
        result.add(this);
        return result;

    }

    public int countOuterVertices(Plane clipPlane){
        int counter = 0;
        boolean[] outside = new boolean[3];
        for(int i = 0; i<3; i++){
            if(Vector3d.dotProduct(Vector3d.sub(getWorldVertex(i), clipPlane.getPoint()), clipPlane.getNormal()) < -0.0001){
                outside[i] = true;
                counter++;
            }
        }

        //Find intersection points
        if(counter == 2){
            Vector3d inPoint = new Vector3d(0f, 0f, 0f);
            Vector3d[] outPoints = new Vector3d[2];

            int outPointIndex = 0;
            for(int i=0; i<3; i++){
                if(!outside[i]){
                    inPoint = worldVertices[i];
                }
                else{
                    outPoints[outPointIndex] = worldVertices[i];
                    outPointIndex++;
                }
            }

            intersecVertices[0] = Vector3d.intersectPoint(Vector3d.sub(outPoints[0], inPoint), inPoint, clipPlane);
            intersecVertices[1] = Vector3d.intersectPoint(Vector3d.sub(outPoints[1], inPoint), inPoint, clipPlane);
            remainingVertices[0] = inPoint;
        }

        if(counter == 1){
            Vector3d outPoint = new Vector3d(0f, 0f, 0f);
            Vector3d[] inPoints = new Vector3d[2];

            int inPointIndex = 0;
            for(int i=0; i<3; i++){
                if(!outside[i]){
                    inPoints[inPointIndex] = worldVertices[i];
                    inPointIndex++;
                }
                else{
                    outPoint = worldVertices[i];
                }
            }

            intersecVertices[0] = Vector3d.intersectPoint(Vector3d.sub(inPoints[0], outPoint), outPoint, clipPlane);
            intersecVertices[1] = Vector3d.intersectPoint(Vector3d.sub(inPoints[1], outPoint), outPoint, clipPlane);
            remainingVertices[0] = inPoints[0];
            remainingVertices[1] = inPoints[1];
        }

        return counter;
    }


    public void setWorldVertex(int index, Vector3d vertex){
        this.worldVertices[index] = vertex;
    }

    public Vector3d getCamVertex(int index) {
        return camVertices[index];
    }

    public Vector3d getWorldVertex(int index){
        return worldVertices[index];
    }

    public Float getDepth(){
        return Vector3d.getNorm(getCenterLocation());
    }

    public Vector3d getCenterLocation(){
        float averageX = (camVertices[0].getX() + camVertices[1].getX() + camVertices[2].getX())*0.333f;
        float averageY = (camVertices[0].getY() + camVertices[1].getY() + camVertices[2].getY())*0.333f;
        float averageZ = (camVertices[0].getZ() + camVertices[1].getZ() + camVertices[2].getZ())*0.333f;

        return new Vector3d(averageX, averageY, averageZ);
    }

    private Color calculateColor(Sun sun){

        //Between ambientLight and 1
        float lightAmount = Vector3d.dotProduct(normal, sun.getLightDirection());
        if(lightAmount < sun.getAmbientLight())
            lightAmount = sun.getAmbientLight();

        float constant = 50; //The higher the value, the more the sun will influence colors [0,255]

        float resultR = lightAmount * triangleColor.getRed() * ((sun.getColor().getRed()+constant) * 0.00392157f);
        if(resultR > 255)
            resultR = 255;

        float resultG = lightAmount * triangleColor.getGreen() * ((sun.getColor().getGreen()+constant)  * 0.00392157f);
        if(resultG > 255)
            resultG = 255;

        float resultB = lightAmount * triangleColor.getBlue() * ((sun.getColor().getBlue()+constant)  * 0.00392157f);
        if(resultB > 255)
            resultB = 255;

        return new Color((int)resultR, (int)resultG, (int)resultB);
    }

    public void drawTriangle(Graphics g, Sun sun){
        g.setColor(calculateColor(sun));
        g.fillPolygon(xPoints, yPoints, 3);
    }

    public void drawWireFrame(Graphics g, Color wireColor){
        g.setColor(wireColor);
        g.drawPolygon(xPoints, yPoints, 3);
    }



}
