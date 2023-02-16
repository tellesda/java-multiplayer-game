package debug;

import math.Vector2D;
import object.Camera;
import object.Entity;
import scene.World;
import java.awt.Color;
import java.awt.Graphics;

public class DebugPoint extends Entity {

    public float size;
    public int maxFrames;
    public int frames;
    public Color color;

    public DebugPoint(float size, int timeSeconds, Vector2D location, Color color, World parentWorld){
        super(location, new Vector2D(size, size), null);
        this.maxFrames =timeSeconds*parentWorld.getParentEngine().fps;
        this.color = color;

    }

    @Override
    public void init() {

    }

    @Override
    public void render(Graphics g, int width, int height, World world) {

        Camera camera = world.getCamera();
        float cameraScaling = 64/camera.getScaling();

        Vector2D worldSpace = Vector2D.sub(location, new Vector2D(scale.getX()*0.5f, scale.getY()*0.5f));
        Vector2D camSpace = Vector2D.add(camera.getInversePosition(), worldSpace);
        Vector2D sized = Vector2D.add(Vector2D.mul(camSpace, new Vector2D(cameraScaling, cameraScaling)),
                new Vector2D(width*0.5f, height*0.5f));

        int resultWidth = (int)(scale.getX()*cameraScaling);
        int resultHeight = (int)(scale.getY()*cameraScaling);

        g.setColor(color);
        g.fillRect((int)sized.getX()-1, (int)sized.getY()-1, resultWidth+1, resultHeight+1);

        frames++;
    }
}
