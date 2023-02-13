package rendering;

import math.Vector2D;
import object.Entity;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class TextureModifier {

    public static BufferedImage copyImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics2D g = b.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public static BufferedImage applyShadowMap(BufferedImage source, ShadowMap shadowMap, Entity entity){

        BufferedImage texture = copyImage(source);
        for(int y=0; y<texture.getHeight(); y++){
            for(int x=0; x<texture.getWidth(); x++){
                int sourceRGB = source.getRGB(x,y);
                if(sourceRGB == 0) //no need to modify if the pixel is black
                    continue;
                Vector2D localPos = relativePixelLocation(x,y,entity.getScale());
                Vector2D worldPos = Vector2D.add(entity.getLocation(), localPos);
                int resultRGB = mixPixel(sourceRGB, shadowMap.getRGB(worldPos));
                texture.setRGB(x,y, resultRGB);
            }
        }
        return texture;
    }


    public static Vector2D relativePixelLocation(int x, int y, Vector2D entityScale){
        float pX = (((x+1)/16f)-(entityScale.getX()/2))* entityScale.getX();
        float pY = (((y+1)/16f)-(entityScale.getY()/2))* entityScale.getY();
        return new Vector2D(pX, pY);
    }

    public static BufferedImage paintTexture(BufferedImage source, Color color){
        BufferedImage texture = copyImage(source);
        int newColor = addAlpha(color.getRGB());
        for(int y=0; y<source.getHeight(); y++){
            for(int x=0; x<source.getWidth(); x++){
                int sourceRGB = source.getRGB(x,y);
                if(sourceRGB == 0) //no need to modify if the pixel is black
                    continue;
                texture.setRGB(x,y, mixPixel(sourceRGB, newColor));
            }
        }
        return texture;
    }

    private static int mixPixel(int x, int y) {
        int xb = (x) & 0xFF;
        int yb = (y) & 0xFF;
        int b = (xb * yb) / 255;

        int xg = (x >> 8) & 0xFF;
        int yg = (y >> 8) & 0xFF;
        int g = (xg * yg) / 255;

        int xr = (x >> 16) & 0xFF;
        int yr = (y >> 16) & 0xFF;
        int r = (xr * yr) / 255;

        int xa = (x >> 24) & 0xFF;
        int ya = (y >> 24) & 0xFF;
        int a = Math.min(255, xa + ya);

        return (b) | (g << 8) | (r << 16) | (a << 24);
    }

    public static int addAlpha(int rgb){
        return rgb + 16777216;
    }

}
