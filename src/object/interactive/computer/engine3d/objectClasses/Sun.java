package object.interactive.computer.engine3d.objectClasses;


import object.interactive.computer.engine3d.mathClasses.*;

import java.awt.Color;

public class Sun{

    //Attributes
    private Rotator3d rotation;
    private Vector3d lightDirection;

    private final Color color;
    private final float ambientLight;

    //Constructor
    public Sun(Rotator3d rotation, Color color, float ambientLight){
        this.rotation = rotation;
        this.color = color;
        this.ambientLight = ambientLight;
        this.lightDirection = new Vector3d(0f,0f,0f);
    }

    public Rotator3d getRotation() { return rotation; }
    public Color getColor() {
        return color;
    }
    public Vector3d getLightDirection() {
        return lightDirection;
    }
    public float getAmbientLight() {
        return ambientLight;
    }

    public void setRotation(Rotator3d rotation) {
        this.rotation = rotation;
    }
    public void rotateX(float amount){
        setRotation(new Rotator3d(getRotation().getX()+amount, getRotation().getY(), getRotation().getZ()));
    }
    public void rotateZ(float amount){
        setRotation(new Rotator3d(getRotation().getX(), getRotation().getY(), getRotation().getZ()+amount));
    }

    public void setLightDirection(Matrix inv){

        Vector3d forwardVector = new Vector3d(1f,0f,0f);

        Matrix worldSpace = Matrix.mul(Matrix.getRotMatrix(rotation), forwardVector.toMatrix4x1());

        Matrix converted = Matrix.mul(Matrix.extractRotation(inv), worldSpace);

        this.lightDirection = new Vector3d(converted.getElement(0,0),
                                           converted.getElement(1,0),
                                           converted.getElement(2,0));
    }

    //Init (Runs once)
    public void init(){

    }

    //Tick (Runs every frame)
    public void tick(){

    }

}
