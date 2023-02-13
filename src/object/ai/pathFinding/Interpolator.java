package object.ai.pathFinding;

import math.Vector2D;

public class Interpolator {

    private String direction;
    private final int speed;
    private long lastTime,timer;

    private final Vector2D end;
    private int nTicks = 10;
    private Vector2D current;
    private boolean finished;

    private final float stepX, stepY;

    public Interpolator(Vector2D start, Vector2D end, int speed){
        setDirection(start, end);
        this.speed = speed;
        timer = 0;
        lastTime = System.currentTimeMillis();
        this.end = end;
        this.stepX = (end.getX() - start.getX())/nTicks;
        this.stepY = (end.getY() - start.getY())/nTicks;
        current = start;
    }

    private void setDirection(Vector2D start, Vector2D end){
        if(start.getY() > end.getY())
            this.direction = "UP";
        else
            this.direction = "DOWN";
    }

    public Vector2D getEnd() {
        return end;
    }

    public String getDirection() {
        return direction;
    }

    public Vector2D getCurrent() {
        return current;
    }

    public boolean isFinished() {
        return finished;
    }

    public void tick(){

        if(isFinished())
            return;

        timer += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();

        if(timer > speed){
            timer = 0;

            //do stuff here...
            if(nTicks <= 0){
                finished = true;
                return;
            }

            current = new Vector2D( current.getX() + stepX, current.getY() + stepY);
            nTicks--;
        }
    }

}

