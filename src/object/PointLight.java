package object;

import anim.Assets;
import math.Vector2D;
import rendering.TextureModifier;
import scene.World;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class PointLight extends Entity{

    private final Color color;
    private final float radius;
    private final BufferedImage lightTexture;
    private final World parentWorld;

    public PointLight(Vector2D location, float radius, Color color, World parentWorld){
        super(location, new Vector2D(1f,1f), null);
        this.parentWorld = parentWorld;
        this.radius = radius;
        this.color = color;
        this.lightTexture = TextureModifier.paintTexture(Assets.pointLight, color);
    }

    @Override
    public void setLocation(Vector2D location) {
        addBlocksToUpdate();
        super.setLocation(location);
        addBlocksToUpdate();
        parentWorld.getLevel().getShadowMap().updateShadowMap(parentWorld);
    }

    public void drawOnShadowMap(Graphics2D sg){
        sg.setColor(color);
        int halfRadius = (int)(radius*8);
        sg.drawImage(lightTexture,(int)(location.getX()*16-halfRadius), (int)(location.getY()*16-halfRadius), (int)(radius*16), (int)(radius*16), null);
    }

    public void addBlocksToUpdate(){

        int searchRadius = (int)(radius*0.5);
        int startX = Level.getLeftTileIndex(location, searchRadius);
        int endX = Level.getRightTileIndex(location, searchRadius, parentWorld.getLevel().getMapSizeX());
        int startY = Level.getUpTileIndex(location, searchRadius);
        int endY = Level.getBottomTileIndex(location, searchRadius, parentWorld.getLevel().getMapSizeY());

        for(int m = startY; m<endY; m++)
            for(int n = startX; n<endX; n++){
                Block block = parentWorld.getLevel().getTileGrid()[m][n];
                block.updateLight = true;
            }
    }

    public void init(){}

    public void tick(){}

    public void render(Graphics g, int width, int height, World world){
    }
}
