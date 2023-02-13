package object;

import math.Vector2D;
import scene.World;

import java.awt.Graphics;

public class Camera extends Entity{

    //Attributes
    private final float scaling;
    private final World parentWorld;
    private final float verticalScalar, horizontalScalar; //constants used for getting visible tiles

    //Constructor
    public Camera(Vector2D location, World parentWorld){
        super(location, new Vector2D(1f, 1f), null);
        this.scaling = 1 / ((float)parentWorld.getParentEngine().height/720);
        this.parentWorld = parentWorld;
        float aspectRatio = (float)(parentWorld.getParentEngine().height)/(float)(parentWorld.getParentEngine().width);
        this.verticalScalar = ((float)parentWorld.getParentEngine().height/64)*scaling*aspectRatio;
        this.horizontalScalar = ((float)parentWorld.getParentEngine().height/64)*scaling;
    }

    public float getScaling() {
        return scaling;
    }

    public Vector2D getInversePosition(){
        return new Vector2D(-location.getX(), -location.getY());
    }

    @Override
    public void setLocation(Vector2D location) {
            super.setLocation(new Vector2D(location.getX(), location.getY()));
    }

    public int getUpTileIndex(){
        int result = (int)(getLocation().getY()+1 - verticalScalar);
        if(result < 0)
            result = 0;
        return result;
    }

    public int getLeftTileIndex(){
        int result = (int)(getLocation().getX()+1 - horizontalScalar);
        if(result < 0)
            result = 0;
        return result;
    }

    public int getRightTileIndex(){
        int result = (int)(getLocation().getX()+1 + horizontalScalar);
        if(result > parentWorld.getLevel().getMapSizeX()-1)
            result = parentWorld.getLevel().getMapSizeX();
        return result;
    }

    public int getBottomTileIndex(){
        int result = (int)(getLocation().getY()+1 + verticalScalar);
        if(result > parentWorld.getLevel().getMapSizeY()-1)
            result = parentWorld.getLevel().getMapSizeY();
        return result;
    }

    public boolean visible(Vector2D location){
        int locX = (int)location.getX();
        int locY = (int)location.getY();

        return (locX < getRightTileIndex() && locX > getLeftTileIndex() &&
                locY < getBottomTileIndex() && locY > getUpTileIndex());
    }


    public void init(){}

    public void tick(){
        super.tick();
    }

    public void render(Graphics g, int width, int height, World world){}

}
