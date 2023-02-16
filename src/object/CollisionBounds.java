package object;

import math.Vector2D;

import java.util.ArrayList;
import java.util.LinkedList;

public class CollisionBounds {

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

    public boolean intersectsLine(float x1, float y1, float x2, float y2, float offset) {
        float minX = Math.min(x1, x2);
        float minY = Math.min(y1, y2);
        float maxX = Math.max(x1, x2);
        float maxY = Math.max(y1, y2);

        float cx1 = worldStartX-offset;
        float cx2 = worldEndX+offset;
        float cy1 = worldStartY-offset;
        float cy2 = worldEndY+offset;


        // Check if the bounding boxes of the line and rectangle intersect
        if (maxX < cx1 || minX > cx2 || maxY < cy1 || minY > cy2) {
            return false;
        }

        // Check if the line intersects any of the four sides of the rectangle
        if (x1 == x2) {
            // Vertical line
            if (x1 < cx1 || x1 > cx2) {
                return false;
            }
            float t = (y1 - cy1) / (y2 - y1);
            float intersectX = x1 + t * (x2 - x1);
            return intersectX >= cx1 && intersectX <= cx2;
        } else {
            // Non-vertical line
            float m = (y2 - y1) / (x2 - x1);
            float b = y1 - m * x1;
            float intersectY1 = m * cx1 + b;
            float intersectY2 = m * cx2 + b;
            return (!(intersectY1 < cy1) || !(intersectY2 < cy1)) && (!(intersectY1 > cy2) || !(intersectY2 > cy2));
        }
    }

    public Vector2D[] getVisibleEdges(Vector2D from){

        Vector2D[] points = new Vector2D[]{
            new Vector2D(worldStartX, worldStartY),
            new Vector2D(worldStartX, worldEndY),
            new Vector2D(worldEndX, worldEndY),
            new Vector2D(worldEndX, worldStartY),
        };

        int nHidden = 0;

        for(int i=0; i<4; i++){
            Vector2D point = points[i];
            if (nHidden >= 2) {
                break;
            }
            if(intersectsLine(from.getX(), from.getY(), point.getX(), point.getY(), -0.0001f)){
                nHidden++;
                points[i] = null;
            }
        }

        int j = 0;
        Vector2D[] resultPoints = new Vector2D[2];
        for(int i=0; i<4; i++){

            if(j == 2)
                break;

            Vector2D point = points[i];

            if(point != null){

                int nextIdx = (i + 1) % 4;
                int prevIdx = (i - 1) % 4;
                if(i == 0)
                    prevIdx = 3;

                if(nHidden == 2){
                    resultPoints[j] = point;
                    j++;
                }
                else if(points[nextIdx] == null || points[prevIdx] == null){
                    resultPoints[j] = point;
                    j++;
                }
            }
        }

        return resultPoints;

    }



    public boolean isLineInside(Vector2D p1, Vector2D p2){
        return isPointInside(p1) | isPointInside(p2);
    }
}
