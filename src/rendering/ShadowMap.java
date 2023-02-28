package rendering;

import math.Vector2D;
import scene.World;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class ShadowMap {

    private final Color ambientColor;
    private final Graphics2D sg;
    private final BufferedImage shadowMap;

    public ShadowMap(int mapX, int mapY, Color ambientColor){
        this.ambientColor = ambientColor;
        this.shadowMap = new BufferedImage(mapX*16, mapY*16, BufferedImage.TYPE_INT_RGB);
        this.sg = shadowMap.createGraphics();
    }

    public int getRGB(Vector2D worldLocation) {
        float lightMapX = worldLocation.getX()*16;
        float lightMapY = worldLocation.getY()*16;
        try {
            if(lightMapX<0 | lightMapY<0)
                return TextureModifier.addAlpha(ambientColor.getRGB());
            if((int)lightMapX>=shadowMap.getWidth() | (int)lightMapY>=shadowMap.getHeight())
                return TextureModifier.addAlpha(ambientColor.getRGB());

            return TextureModifier.addAlpha(shadowMap.getRGB((int)lightMapX, (int)lightMapY));
        }
        //TODO: do not ignore
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return 0;
    }

    public void drawShadowMap(World world){
        int width = shadowMap.getWidth();
        int height = shadowMap.getHeight();
        sg.setColor(ambientColor);
        sg.fillRect(0,0,width, height);
        for(var light : world.getLevel().getPointLights())
            light.drawOnShadowMap(sg);
    }

}
