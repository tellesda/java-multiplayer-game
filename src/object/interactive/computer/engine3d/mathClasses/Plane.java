package object.interactive.computer.engine3d.mathClasses;

public class Plane {

    //Attributes
    private Vector3d normal;
    private Vector3d point;

    //Constructor
    public Plane(Vector3d normal, Vector3d point){
        this.normal = normal;
        this.point = point;
    }

    public Vector3d getNormal() {
        return normal;
    }

    public Vector3d getPoint() {
        return point;
    }

    public void setPlane(Vector3d normal, Vector3d point){
        this.normal = normal;
        this.point = point;
    }
}
