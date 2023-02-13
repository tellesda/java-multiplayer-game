package object.interactive.computer.snake;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Fruit {

    int x,y;

    public Fruit(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move(Body snakeHead, int gridSize){

        Stack<Integer[]> existingXYs = Body.getSnakeXYs(snakeHead); //Existing snake positions

        boolean[][] snakePresenceGrid = new boolean[gridSize][gridSize];// true if snake is on the tile, false otherwise

        for(var point : existingXYs){
            int pointX = point[0];
            int pointY = point[1];
            snakePresenceGrid[pointY][pointX] = true;
        }

        List<Integer[]> validPoints = new ArrayList<>(gridSize*gridSize); //Valid teleporting points

        for(int m=0; m<gridSize; m++)
            for(int n=0; n<gridSize; n++){
                if(!snakePresenceGrid[m][n])
                    validPoints.add(new Integer[]{n,m});
            }

        //Selects random location
        int size = validPoints.size();
        if(size == 0)
            return;
        Integer[] randomPoint = validPoints.get((int)(Math.random()* size));
        this.x = randomPoint[0];
        this.y = randomPoint[1];
    }
}
