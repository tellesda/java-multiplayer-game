package object;

import math.Vector2D;

public class CollisionBounds {

    private Entity parent;
    //Local coordinates
    private final float startX;
    private final float startY;
    private final float endX;
    private final float endY;

    //World coordinates
    private float worldStartX;
    private float worldStartY;
    private float worldEndX;
    private float worldEndY;

    public CollisionBounds(float startX, float endX, float startY, float endY, Vector2D location){
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        updateWorldCollision(location);
    }

    public void updateWorldCollision(Vector2D location){
        this.worldStartX = location.getX()+startX;
        this.worldStartY = location.getY()+startY;
        this.worldEndX = location.getX()+endX;
        this.worldEndY = location.getY()+endY;
    }

    public boolean isPointInside(Vector2D point){
        if(point.getX() >= worldStartX && point.getX()<=worldEndX){
            return point.getY() >= worldStartY && point.getY() <= worldEndY;
        }
        return false;
    }

    public boolean isLineInside(Vector2D p1, Vector2D p2){
        return isPointInside(p1) | isPointInside(p2);
    }
}
