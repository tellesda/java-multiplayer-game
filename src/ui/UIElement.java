package ui;
import engine.Engine;
import input.KeyManager;
import input.MouseManager;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class UIElement {

    protected final Engine parentEngine;
    public int posX, posY, scaleX, scaleY;
    public BufferedImage texture;

    public UIElement(int posX, int posY, int scaleX, int scaleY, BufferedImage texture, Engine parentEngine){
        this.posX = posX;
        this.posY = posY;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.texture = texture;
        this.parentEngine = parentEngine;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public MouseManager getMouseManager() {
        return parentEngine.getMouseManager();
    }

    public KeyManager getKeyManager(){
        return parentEngine.getKeyManager();
    }

    public Engine getParentEngine() {
        return parentEngine;
    }

    public boolean isMouseOver(){

        int boundLeft = posX-(scaleX/2);
        int boundRight = boundLeft + scaleX;
        int boundTop = posY-(scaleY/2);
        int boundBottom = boundTop + scaleY;

        if(getMouseManager().getMouseX() > boundLeft && getMouseManager().getMouseX() < boundRight){
            return getMouseManager().getMouseY() > boundTop && getMouseManager().getMouseY() < boundBottom;
        }
        return false;
    }

    public boolean isClicked(){
        if(isMouseOver() && getMouseManager().isLeftPressed() && getMouseManager().canClick){
            getMouseManager().canClick = false;
            return true;
        }
        return false;
    }

    public void init(){
    }

    public void tick(){
        if(!getMouseManager().isLeftPressed() && isMouseOver())
            getMouseManager().canClick = true;
    }

    public void render(Graphics g){
        g.drawImage(texture, posX-(scaleX/2), posY-(scaleY/2), scaleX, scaleY, null);
    }
}
