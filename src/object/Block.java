package object;

import anim.Assets;
import math.Vector2D;
import rendering.LitObject;
import rendering.ShadowMap;
import rendering.TextureModifier;
import scene.World;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Block extends Entity implements LitObject {

    private final int blockIndex;
    private BufferedImage blockTexture;
    private BufferedImage blockTextureLit;
    private boolean hasCollision;

    //Constructor
    public Block(Vector2D location, int blockIndex){
        super(location, new Vector2D(1f, 1f), new CollisionBounds(-0.5f,0.5f,-0.5f,0.5f, location));
        this.blockTexture = Assets.mapBlocks[blockIndex];
        this.blockIndex = blockIndex;

        for(int i : Assets.tilesWithCollision)
            if(blockIndex == i)
                hasCollision = true;
    }

    public void setHasCollision(boolean hasCollision) {
        this.hasCollision = hasCollision;
    }

    public void setBlockTexture(int index) {
        this.blockTexture = Assets.mapBlocks[index];
    }

    public boolean hasCollision() {
        return hasCollision;
    }

    public void updateLightTexture(ShadowMap shadowMap){
        this.blockTextureLit = TextureModifier.applyShadowMap(blockTexture, shadowMap, this);
    }

    public void init(){}

    public void tick(){}

    public void render(Graphics g, int width, int height, World world){

        if(blockIndex == 111)
            return; //TODO verify if the texture grid is modified

        Camera camera = world.getCamera();
        float cameraScaling = 64/camera.getScaling();

        Vector2D worldSpace = Vector2D.sub(location, new Vector2D(scale.getX()*0.5f, scale.getY()*0.5f));
        Vector2D camSpace = Vector2D.add(camera.getInversePosition(), worldSpace);
        Vector2D sized = Vector2D.add(Vector2D.mul(camSpace, new Vector2D(cameraScaling, cameraScaling)),
                new Vector2D(width*0.5f, height*0.5f));

        int resultWidth = (int)(scale.getX()*cameraScaling);
        int resultHeight = (int)(scale.getY()*cameraScaling);

        g.drawImage(blockTextureLit, (int)sized.getX()-1, (int)sized.getY()-1, resultWidth+1, resultHeight+1, null);
    }

}
