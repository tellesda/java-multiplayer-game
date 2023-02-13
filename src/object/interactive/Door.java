package object.interactive;

import math.Vector2D;
import network.packetClasses.DoorPacket;
import object.Block;
import object.CollisionBounds;
import object.Player;
import object.Sortable;
import rendering.LitObject;
import rendering.ShadowMap;
import scene.World;

import java.awt.Graphics;

public class Door implements Interactive, LitObject, Sortable {

    private boolean isOpen;
    private final int code;
    private final Block doorTop, doorBottom;
    private final World parentWorld;

    public Door(Vector2D location, World parentWorld, int code){
        this.doorTop = new Block(Vector2D.sub(location, new Vector2D(0,1)), 94);
        this.doorBottom = new Block(location, 108);
        this.doorBottom.setCollision(new CollisionBounds(-0.5f,0.5f,0.4f,0.5f, location));
        this.parentWorld = parentWorld;
        this.code = code;
        close();
    }

    public int getCode() {
        return code;
    }

    public float getY() {
       return this.doorBottom.getLocation().getY()+0.5f;
    }

    public Block getDoorTop() {
        return doorTop;
    }

    public Block getDoorBottom() {
        return doorBottom;
    }

    public void setLocation(Vector2D location) {
        doorTop.setLocation(Vector2D.sub(location, new Vector2D(0,2)));
        doorBottom.setLocation(location);
    }


    public void updateLightTexture(ShadowMap shadowMap) {
        doorBottom.updateLightTexture(shadowMap);
        doorTop.updateLightTexture(shadowMap);
    }

    public Vector2D getLocation() {
        return doorBottom.getLocation();
    }

    public boolean isOpen(){
        return this.isOpen;
    }

    public void open(){
        isOpen = true;
        this.doorBottom.setHasCollision(false);
        this.doorBottom.setBlockTexture(109);
        this.doorTop.setBlockTexture(95);
        updateLightTexture(parentWorld.getLevel().getShadowMap());
    }

    public void close(){
        isOpen = false;
        this.doorBottom.setHasCollision(true);
        this.doorBottom.setBlockTexture(108);
        this.doorTop.setBlockTexture(94);
        updateLightTexture(parentWorld.getLevel().getShadowMap());
    }

    public void init() {

    }


    public void tick() {

    }


    public void render(Graphics g, int width, int height, World world) {
        doorTop.render(g,width,height,world);
        doorBottom.render(g,width,height,world);
    }


    public void interact(Player player) {

        if(parentWorld.getParentEngine().gameClient.getClient().isConnected()){
            //Write a packet to the server
            DoorPacket packet = new DoorPacket();
            packet.doorCode = (byte)code;
            packet.open = !isOpen;
            parentWorld.getParentEngine().gameClient.sendPacket(packet);
        }
        else{
            //Open the door directly
            if(isOpen)
                close();
            else
                open();
        }

        player.setInteracting(false);
    }
}
