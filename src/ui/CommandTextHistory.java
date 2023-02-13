package ui;

import engine.Engine;

import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Queue;


public class CommandTextHistory extends UIElement{

    private int textVisibleTimer;
    private final int maxHistory = 8;
    private final Queue<TextBubble> queue;

    public CommandTextHistory(int posX, int posY, int scaleX, int scaleY, Engine parentEngine){
        super(posX, posY, scaleX, scaleY, null, parentEngine);
        this.queue = new ArrayDeque<>(maxHistory);
        for(int i=0; i<maxHistory; i++){
            queue.add(new TextBubble("", Color.white));
        }
    }

    public void addElement(TextBubble text){
        queue.add(text);
        if(queue.size() > maxHistory){
            queue.remove();
        }
        textVisibleTimer = 0;
    }

    public void render(Graphics g, boolean chatVisible) {
        int resultX = posX-(scaleX/2);
        int resultY = posY-(scaleY/2);


        if(chatVisible){
            g.setColor(new Color(0,0,0,100));
            g.fillRoundRect(resultX, resultY, scaleX, scaleY, 10, 10);
            textVisibleTimer = 0;
        }

        if (textVisibleTimer <= this.parentEngine.fps*4) { //Draw text
            textVisibleTimer++;
            int y = posY - (int)(scaleY*0.45);
            for(var text : queue){
                g.setColor(text.getColor());
                g.drawString(text.getText(), posX-(int)(scaleX*0.45), y);
                y += scaleY/maxHistory;
            }
        }


    }
}
