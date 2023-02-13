package ui;

import anim.Assets;
import engine.Engine;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class Button extends UIElement{

    private String text;
    public static boolean lock;
    public boolean alignLeft;

    public Button(String text, BufferedImage texture, int posX, int posY, int scaleX, int scaleY, Engine parentEngine){
        super(posX, posY, scaleX, scaleY, Assets.button, parentEngine);
        if(texture != null)
            this.texture = texture;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAlignLeft(boolean alignLeft) {
        this.alignLeft = alignLeft;
    }

    @Override
    public boolean isClicked(){
        if(isMouseOver() && getMouseManager().isLeftPressed() && getMouseManager().canClick && !Button.lock){
            getMouseManager().canClick = false;
            return true;
        }
        return false;
    }


    @Override
    public void render(Graphics g){

        int resultX = posX-(scaleX/2);
        int resultY = posY-(scaleY/2);

        g.setColor(Color.white);

        int selectPadding = 2;
        if(isMouseOver() && !getMouseManager().isLeftPressed()){
            g.fillRect(resultX-selectPadding, resultY-selectPadding, scaleX+selectPadding*2, scaleY+selectPadding*2);
        }

        g.drawImage(texture, resultX, resultY, scaleX, scaleY, null);

        if(text != null){
            int stringWidth = g.getFontMetrics().stringWidth(text);
            int textPosX;
            int textPosY = posY + (scaleY/8);
            if(alignLeft)
                textPosX = posX-(scaleX/2)+(int)(scaleX*0.1);
            else{
                textPosX = posX-(stringWidth/2);
            }
            g.drawString(text, textPosX, textPosY);
        }
    }
}
