package math;

public class Vector2D{

    float x,y;

    public Vector2D(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }


    public void print(){
        System.out.println("X: " + this.x + ", Y: " + this.y);
    }

    public static Vector2D add(Vector2D p1, Vector2D p2){
        return new Vector2D(p1.x + p2.x, p1.y + p2.y);
    }

    public static Vector2D sub(Vector2D p1, Vector2D p2){
        return new Vector2D(p1.x - p2.x, p1.y - p2.y);
    }

    public static Vector2D mul(Vector2D p1, Vector2D p2){
        return new Vector2D(p1.x * p2.x, p1.y * p2.y);
    }

    public static Vector2D div(Vector2D p1, Vector2D p2){
        return new Vector2D(p1.x / p2.x, p1.y / p2.y);
    }

    public static float getNorm(Vector2D v){
        return (float)Math.sqrt(v.getX() * v.getX() + v.getY() * v.getY());
    }

    public static float distance(Vector2D v1, Vector2D v2){
        return getNorm(sub(v2,v1));
    }

    public static float fastDistance(Vector2D v1, Vector2D v2){
        return Math.abs(v2.x - v1.x) + Math.abs(v2.y - v1.y);
    }
}
