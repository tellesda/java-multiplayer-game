package object;

import anim.Assets;
import math.LineTrace;
import math.Vector2D;
import rendering.LitObject;
import rendering.ShadowMap;
import rendering.TextureModifier;
import scene.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Block extends Entity implements LitObject {

    private final int blockIndex;
    private BufferedImage blockTexture;
    private BufferedImage blockTextureLit;
    private boolean hasCollision;
    public int m, n;

    //Constructor
    public Block(Vector2D location, int blockIndex){
        super(location, new Vector2D(1f, 1f), new CollisionBounds(-0.5f,0.5f,-0.5f,0.5f, location));
        this.blockTexture = Assets.mapBlocks[blockIndex];
        this.blockIndex = blockIndex;

        for(int i : Assets.tilesWithCollision)
            if (blockIndex == i) {
                hasCollision = true;
                break;
            }
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

    public void perPixelRender(Graphics g, int width, int height, World world){

        Vector2D pov = world.getPlayer().getFeetLocation();
        Camera camera = world.getCamera();
        float cameraScaling = 64/camera.getScaling();

        int mapX = world.getLevel().getMapSizeX();
        int mapY = world.getLevel().getMapSizeY();

        for(int y = 0; y < blockTextureLit.getHeight(); y++)
            for (int x=0; x < blockTextureLit.getWidth(); x++){
                Vector2D pixelLocation = TextureModifier.relativePixelLocation(x, y, scale);
                Vector2D wsPixelLocation = Vector2D.add(location, pixelLocation);
                wsPixelLocation = Vector2D.add(wsPixelLocation, new Vector2D(-0.0625f,-0.0625f));

                if(LineTrace.canSee(wsPixelLocation, pov, this, world.getLevel().getTileGrid(), new boolean[mapY][mapX], mapY, mapX)){
                    int pixelColor = blockTextureLit.getRGB(x,y);
                    g.setColor(new Color(pixelColor));
                    Vector2D camSpace = Vector2D.add(camera.getInversePosition(), wsPixelLocation);
                    Vector2D sized = Vector2D.add(Vector2D.mul(camSpace, new Vector2D(cameraScaling, cameraScaling)),
                            new Vector2D(width*0.5f, height*0.5f));

                    int resultWidth = (int)((0.0625f) * cameraScaling);
                    int resultHeight = (int)(0.0625f*cameraScaling);

                    g.fillRect((int)sized.getX()-1, (int)sized.getY()-1, resultWidth+1, resultHeight+1);
                }
            }
    }

    public void render(Graphics g, int width, int height, World world){

        if(blockIndex == 111)
            return; //TODO verify if the texture grid is modified

       // Vector2D pov = world.getPlayer().getFeetLocation();
        //int mapX = world.getLevel().getMapSizeX();
        //int mapY = world.getLevel().getMapSizeY();

        //int nIntersect = 0;
        //for(var edge : collision.getVisibleEdges(pov)){
        //    if(!LineTrace.canSee(edge, pov, this, world.getLevel().getTileGrid(),
        //            new boolean[mapY][mapX], mapY, mapX)){
        //        nIntersect++;
        //    }
        //}

        //if(nIntersect == 2)
        //    return;

        //if(nIntersect == 1){
        //    perPixelRender(g, width, height, world);
        //    return;
        //}

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
