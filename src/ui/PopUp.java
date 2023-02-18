package ui;

import engine.Engine;
import java.awt.Graphics;
import java.awt.Color;

public class PopUp {

    private final int centerX;
    private final int centerY;
    private final int scaleX = 500;
    private final int scaleY = 150;

    public Engine engine;
    public String message;
    public Button button;

    public PopUp(String message, Engine engine){
        this.engine = engine;
        this.centerX = engine.width/2;
        this.centerY = engine.height/2;
        this.button = new Button("Ok", null, centerX, centerY+(scaleY/3), 100, 30, engine);
        this.message = message;
    }

    public void closePopUp(){
        engine.popUp = null;
    }

    public void tick(){
        button.tick();
        if(button.isClicked()){
            closePopUp();
        }
    }

    public void render(Graphics g){
        g.setColor(new Color(0,0,0,125));
        g.fillRect(0,0, engine.width, engine.height);
        int resultX = centerX-(scaleX/2);
        int resultY = centerY-(scaleY/2);
        g.setColor(Color.darkGray);
        g.fillRoundRect(resultX,resultY,scaleX,scaleY, 50, 50);

        g.setColor(Color.white);

        int stringWidth = g.getFontMetrics().stringWidth(message);
        g.drawString(message, centerX-(stringWidth/2), centerY);
        button.render(g);
    }


}
