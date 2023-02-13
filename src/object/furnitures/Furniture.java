package object.furnitures;

import math.Vector2D;
import object.Block;
import object.CollisionBounds;
import object.Entity;
import object.Sortable;
import rendering.LitObject;
import rendering.ShadowMap;
import scene.World;
import java.awt.Graphics;
import java.util.List;

public abstract class Furniture extends Entity implements LitObject, Sortable {

    private int direction; //1:front, 2:back, 3:left, 4:right

    protected List<Block> blocksFront;
    protected List<Block> blocksBack;
    protected List<Block> blocksLeft;
    protected List<Block> blocksRight;

    public Furniture(Vector2D location){
        super(location, new Vector2D(1,1), new CollisionBounds(-1, 1, -1, 0.5f, location));
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public float getY() {
        return this.location.getY();
    }

    public List<Block> getBlocks() {

        List<Block> blocks;

        switch (direction){
            case 1 -> blocks = blocksBack;
            case 2 -> blocks = blocksLeft;
            case 3 -> blocks = blocksRight;
            default -> blocks = blocksFront;
        }

        return blocks;
    }

    public void render(Graphics g, int width, int height, World world) {
        for(var block : getBlocks())
            block.render(g, width, height, world);
    }


    public void updateLightTexture(ShadowMap shadowMap) {
        for(var block : getBlocks())
            block.updateLightTexture(shadowMap);
    }


    public Vector2D getLocation() {
        return location;
    }

    public void init() {

    }
}
