package object;

import anim.Assets;
import math.Vector2D;
import rendering.LitObject;
import rendering.TextureModifier;
import scene.World;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PointLight extends Entity{

    private final Color color;
    private final float radius;
    private final BufferedImage lightTexture;
    private final World parentWorld;
    private List<LitObject> nearbyLitObjects;

    public PointLight(Vector2D location, float radius, Color color, World parentWorld){
        super(location, new Vector2D(1f,1f), null);
        this.parentWorld = parentWorld;
        this.radius = radius;
        this.color = color;
        this.lightTexture = TextureModifier.paintTexture(Assets.pointLight, color);
    }

    @Override
    public void setLocation(Vector2D location) {
        super.setLocation(location);
        updateNearbyLitObjects();
        parentWorld.getLevel().getShadowMap().drawShadowMap(parentWorld);
    }

    public void drawOnShadowMap(Graphics2D sg){
        sg.setColor(color);
        int halfRadius = (int)(radius*8);
        sg.drawImage(lightTexture,(int)(location.getX()*16-halfRadius), (int)(location.getY()*16-halfRadius), (int)(radius*16), (int)(radius*16), null);
    }

    public void updateNearbyLitObjects(){
        nearbyLitObjects = new ArrayList<>();
        for(var litObject : parentWorld.getLevel().getLitObjects()){
            if(Vector2D.distance(litObject.getLocation(), location) < radius+0.6)
                nearbyLitObjects.add(litObject);
        }
    }

    public void drawLight(){
        for(var litObject : nearbyLitObjects)
            litObject.updateLightTexture(parentWorld.getLevel().getShadowMap());
    }

    public void init(){}

    public void tick(){}

    public void render(Graphics g, int width, int height, World world){
    }
}
