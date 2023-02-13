package object.interactive.computer.engine3d.mathClasses;

public class Rotator3d {

    //Attributes
    private Float x;
    private Float y;
    private Float z;

    //Constructor
    public Rotator3d(Float x, Float y, Float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    //Getters
    public Float getX() { return x; }
    public Float getY() { return y; }
    public Float getZ() { return z; }

    //Setters
    public void setRotator(Float x, Float y, Float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void print(){
        System.out.println("X : " + x + "," + "Y : " + y + "," + "Z : " + z);
    }

    //Static methods
    public Matrix toMatrix4x1(){
        Matrix result = new Matrix(4,1);
        result.set(0,0, x);
        result.set(1,0, y);
        result.set(2,0, z);
        result.set(3,0, 1f);
        return result;
    }

    public static Rotator3d combine(Rotator3d r1, Rotator3d r2){

        float x = r1.getX() + r2.getX();
        float y = r1.getY() + r2.getY();
        float z = r1.getZ() + r2.getZ();

        return new Rotator3d(x, y, z);

    }

    public static Rotator3d delta(Rotator3d r1, Rotator3d r2){

        float x = r1.getX() - r2.getX();
        float y = r1.getY() - r2.getY();
        float z = r1.getZ() - r2.getZ();

        return new Rotator3d(x, y, z);

    }

    public static Rotator3d degToRad(Rotator3d r){

        Rotator3d result = new Rotator3d(null, null, null);

        float x = r.getX() * ((float)Math.PI / 180);
        float y = r.getY() * ((float)Math.PI / 180);
        float z = r.getZ() * ((float)Math.PI / 180);

        result.setRotator(x, y ,z);

        return result;
    }

    public static Rotator3d radToDeg(Rotator3d r){

        Rotator3d result = new Rotator3d(null, null, null);

        float x = r.getX() * (180 / (float)Math.PI);
        float y = r.getY() * (180 / (float)Math.PI);
        float z = r.getZ() * (180 / (float)Math.PI);

        result.setRotator(x, y ,z);

        return result;
    }

    public static Rotator3d getRotationByYawPitch(float yaw, float pitch){

        Matrix rotX = Matrix.getRotXMatrix(-pitch);
        Matrix rotY = Matrix.getRotYMatrix(yaw);
        Matrix rotationMatrix = Matrix.mul(rotX, rotY);

        float r11 = rotationMatrix.getElement(0,0);
        float r21 = rotationMatrix.getElement(1,0);
        float r31 = rotationMatrix.getElement(2,0);
        float r32 = rotationMatrix.getElement(2,1);
        float r33 = rotationMatrix.getElement(2,2);

        return new Rotator3d((float)Math.atan2(r32, r33),
                             (float)Math.atan2(-r31,Math.sqrt(r32*r32 + r33*r33)),
                             (float)Math.atan2(r21,r11));

    }

    public static void print(Rotator3d r){
        System.out.println("X : " + r.getX() + "," + "Y : " + r.getY() + "," + "Z : " + r.getZ());
    }

}
