package object.interactive.computer.engine3d.mathClasses;

public class Vector3d {

    //Attributes
    private float x;
    private float y;
    private float z;

    //Constructor
    public Vector3d(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    //Getters
    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }

    //Instance methods

    public void setVector(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Matrix toMatrix4x1(){
        Matrix result = new Matrix(4,1);
        result.set(0,0, x);
        result.set(1,0, y);
        result.set(2,0, z);
        result.set(3,0, 1f);
        return result;
    }

    public void print(){
        System.out.println("X : " + x + "," + "Y : " + y + "," + "Z : " + z);
    }


    //Static methods
    public static Vector3d scale(Vector3d vector, float scalar){
        return new Vector3d(vector.x * scalar, vector.y * scalar, vector.z * scalar);
    }

    public static Vector3d add(Vector3d v1, Vector3d v2){

        return new Vector3d(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);

    }
    public static Vector3d sub(Vector3d v1, Vector3d v2){

        return new Vector3d(v1.x-v2.x, v1.y-v2.y, v1.z-v2.z);

    }
    public static Vector3d mul(Vector3d v1, Vector3d v2){

        return new Vector3d(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);

    }
    public static Vector3d div(Vector3d v1, Vector3d v2){

        return new Vector3d(v1.x / v2.x, v1.y / v2.y, v1.z / v2.z);

    }

    public static Vector3d normalize(Vector3d v){
        float invNorm = 1/Vector3d.getNorm(v);
        return new Vector3d(v.getX()*invNorm, v.getY()*invNorm, v.getZ()*invNorm);
    }

    public static float getNorm(Vector3d v){

        return (float)Math.sqrt(v.getX() * v.getX() + v.getY() * v.getY() + v.getZ() * v.getZ());
    }

    public static Vector3d crossProduct(Vector3d v1, Vector3d v2){

        return new Vector3d(
                v1.getY()*v2.getZ() - v2.getY()*v1.getZ(),
                v2.getX()*v1.getZ() - v1.getX()*v2.getZ(),
                v1.getX()*v2.getY() - v2.getX()*v1.getY());
    }

    public static float dotProduct(Vector3d v1, Vector3d v2){
        return v1.getX()*v2.getX() + v1.getY()*v2.getY() + v1.getZ()*v2.getZ();
    }

    public static Vector3d rotateVector(Vector3d vector, Rotator3d rotation){

        return Matrix.fastMul(Matrix.getRotMatrix(rotation), vector);
    }

    public static Vector3d intersectPoint(Vector3d lineDirection, Vector3d linePoint, Plane plane) {
        Vector3d diff = Vector3d.sub(linePoint, plane.getPoint());
        float prod1 = Vector3d.dotProduct(diff, plane.getNormal());
        float prod2 = Vector3d.dotProduct(lineDirection, plane.getNormal());
        return Vector3d.sub(linePoint, Vector3d.scale(lineDirection, prod1 / prod2));
    }

    public static float distance(Vector3d v1, Vector3d v2){
        return Vector3d.getNorm(Vector3d.sub(v1, v2));
    }


}
