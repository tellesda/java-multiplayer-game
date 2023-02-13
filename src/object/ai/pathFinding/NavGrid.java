package object.ai.pathFinding;

import math.Vector2D;
import object.Block;
import scene.World;

import java.util.ArrayList;
import java.util.List;


public class NavGrid {

    int row;
    int col;
    List<int[]> collisions;

    public NavGrid(World world){
        this.col = world.getLevel().getMapSizeX();
        this.row = world.getLevel().getMapSizeY();
        Block[][] worldTiles = world.getLevel().getTileGrid();

        collisions = new ArrayList<>();
        for(int j=0; j<row; j++)
            for (int i=0; i<col; i++){
                if(worldTiles[j][i].hasCollision())
                    collisions.add(new int[]{j,i});
            }
        //Include furniture collision
        for(var fur : world.getLevel().getFurnitures())
            for(var block : fur.getBlocks())
                if(block.hasCollision())
                    collisions.add(new int[]{(int)block.getLocation().getY(), (int)block.getLocation().getX()});
    }

    public List<Node> getPath(Vector2D start, Vector2D end){
        int startRow = (int)start.getY();
        int startCol = (int)start.getX();
        int endRow = (int)end.getY();
        int endCol = (int)end.getX();
        AStar aStar = new AStar(row,col, new Node(startRow, startCol), new Node(endRow, endCol), false);
        aStar.setBlocks(this.collisions);
        return aStar.findPath();
    }
}
