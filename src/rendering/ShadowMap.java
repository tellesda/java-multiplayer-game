package rendering;

import math.Vector2D;
import scene.World;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Composite;
import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;

public class ShadowMap {

    private final World world;
    private final Color ambientColor;
    private Graphics2D sg;
    private BufferedImage shadowMap;
    private Composite defaultComposite;

    public ShadowMap(int mapX, int mapY, Color ambientColor, World world){
        this.world = world;
        this.ambientColor = ambientColor;

        if(world.getParentEngine().isServer)
            return;

        this.shadowMap = new BufferedImage(mapX*16, mapY*16, BufferedImage.TYPE_INT_RGB);
        this.sg = shadowMap.createGraphics();
        this.defaultComposite = sg.getComposite();
    }

    public int getRGB(Vector2D worldLocation) {

        if(world.getParentEngine().isServer)
            return 0;

        float lightMapX = worldLocation.getX()*16;
        float lightMapY = worldLocation.getY()*16;

        if(lightMapX<0 || lightMapY<0)
            return TextureModifier.addAlpha(ambientColor.getRGB());
        if((int)lightMapX>=shadowMap.getWidth() || (int)lightMapY>=shadowMap.getHeight())
            return TextureModifier.addAlpha(ambientColor.getRGB());

        return TextureModifier.addAlpha(shadowMap.getRGB((int)lightMapX, (int)lightMapY));
    }

    public void updateShadowMap(World world){

        if(world.getParentEngine().isServer)
            return;

        int width = shadowMap.getWidth();
        int height = shadowMap.getHeight();
        sg.setComposite(defaultComposite);
        sg.setColor(ambientColor);
        sg.fillRect(0,0,width, height);

        sg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        for(var light : world.getLevel().getPointLights())
            light.drawOnShadowMap(sg);
    }

}
