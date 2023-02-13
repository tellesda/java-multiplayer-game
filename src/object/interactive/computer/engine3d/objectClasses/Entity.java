package object.interactive.computer.engine3d.objectClasses;
import object.interactive.computer.engine3d.mathClasses.*;

public abstract class Entity {

    //Attributes
    protected Vector3d location;
    protected Rotator3d rotation;
    protected Vector3d scale;

    //Getters
    public Vector3d getLocation() { return location; }
    public Rotator3d getRotation() { return rotation; }
    public Vector3d getScale() { return scale; }

    //Setters
    public void setPosition(Vector3d position) { this.location = position; }
    public void setRotation(Rotator3d rotation) { this.rotation = rotation; }
    public void setScale(Vector3d scale) { this.scale = scale; }

    //Methods
    public void addWorldLocation(Vector3d deltaLocation){
        setPosition(Vector3d.add(getLocation(), deltaLocation));
    }

    public void moveX(float amount){
        setPosition(new Vector3d(getLocation().getX()+amount, getLocation().getY(), getLocation().getZ()));
    }
    public void moveY(float amount){
        setPosition(new Vector3d(getLocation().getX(), getLocation().getY()+amount, getLocation().getZ()));
    }
    public void moveZ(float amount){
        setPosition(new Vector3d(getLocation().getX(), getLocation().getY(), getLocation().getZ()+amount));
    }

    public void rotateX(float amount){
        setRotation(new Rotator3d(getRotation().getX()+amount, getRotation().getY(), getRotation().getZ()));
    }
    public void rotateY(float amount){
        setRotation(new Rotator3d(getRotation().getX(), getRotation().getY()+amount, getRotation().getZ()));
    }
    public void rotateZ(float amount){
        setRotation(new Rotator3d(getRotation().getX(), getRotation().getY(), getRotation().getZ()+amount));
    }

    public Vector3d getForwardVector(){
        return Vector3d.rotateVector(new Vector3d(0f,0f,1f), getRotation());
    }
    public Vector3d getRightVector(){
        return Vector3d.rotateVector(new Vector3d(1f,0f,0f), getRotation());
    }
    public Vector3d getUpVector(){
        return Vector3d.rotateVector(new Vector3d(0f,1f,0f), getRotation());
    }

}
