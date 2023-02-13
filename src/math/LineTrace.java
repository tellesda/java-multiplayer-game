package math;

import object.Entity;

import java.util.List;

public class LineTrace {

    public static final int precision = 10;

    public static boolean canSee(Vector2D start, Vector2D end, List<Entity> entities){
        float stepY = (end.y - start.y)/precision;
        float stepX = (end.x - start.x)/precision;

        for(int i=0; i<precision; i++){
            Vector2D checkPosition = Vector2D.add(start, new Vector2D(stepX*i, stepY*i));
            for(var entity : entities){
                if(entity.getCollision() == null)
                    continue;
                if(entity.getCollision().isPointInside(checkPosition))
                    return false;
            }
        }
        return true;
    }


}
