package math;

import object.Block;

public class LineTrace {


    public static boolean canSee(Vector2D start, Vector2D target, Block current, Block[][] tiles, boolean[][] visited, int mapY, int mapX){

        if(Vector2D.fastDistance(current.getLocation(), target) <= 1)
            return true;

        visited[current.m][current.n] = true;

        int startM;
        int startN;
        int endM = Math.min(current.m+2, mapY);
        int endN = Math.min(current.n+2, mapX);

        Vector2D relativeDirection = Vector2D.sub(target, current.getLocation());

        if(relativeDirection.getY() > 1)
            startM = current.m;
        else
            startM = Math.max(current.m-1, 0);

        if(relativeDirection.getX() > 1)
            startN = current.n;
        else
            startN = Math.max(current.n-1, 0);

        for(int m=startM; m<endM; m++){
            for(int n=startN; n<endN; n++){

                if(visited[m][n])
                    continue;

                visited[m][n] = true;

                Block next = tiles[m][n];

                if(next.getCollision().intersectsLine(
                        start.getX(), start.getY(),
                        target.getX(), target.getY(), -0.0001f))
                {
                    if(next.hasCollision())
                        return false;

                    return canSee(start, target, next, tiles, visited, mapY, mapX);
                }
            }
        }
        return true;
    }


}
