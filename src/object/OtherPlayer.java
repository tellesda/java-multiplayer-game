package object;

import math.Vector2D;
import scene.World;

import java.awt.Color;
import java.awt.Graphics;

public class OtherPlayer extends Human{

    public String name;
    public int id;

    public OtherPlayer(World world){
        super(new Vector2D(0f, 0f), world, "Joe");
    }


    public void setLocation(Vector2D location) {
        super.setLocation(location);
    }

    @Override
    public void render(Graphics g, int width, int height, World world) {
        Camera camera = world.getCamera();

        float cameraScaling = 1/camera.getScaling();
        Vector2D worldSpace = Vector2D.sub(location, new Vector2D(scale.getX()*0.5f, scale.getY()*0.5f));
        Vector2D camSpace = Vector2D.add(camera.getInversePosition(), worldSpace);
        Vector2D sized = Vector2D.add(Vector2D.mul(camSpace, new Vector2D(64*cameraScaling, 64*cameraScaling)),
                new Vector2D(width*0.5f, height*0.5f));

        int resultWidth = (int)(64*scale.getX()*cameraScaling);
        int resultHeight = (int)(64*scale.getY()*cameraScaling);

        //Update lit texture half the frames
        if(timer.ticking)
            updateTextureLit(world.getLevel().getShadowMap());

        g.drawImage(textureLit, (int)sized.getX(), (int)sized.getY(), resultWidth, resultHeight, null);
        String label = name + "(ID: " + id + ")";
        g.setColor(Color.white);
        g.drawString(label, (int)sized.getX(), (int)sized.getY());
    }
}
