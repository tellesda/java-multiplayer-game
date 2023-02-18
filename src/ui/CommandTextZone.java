package ui;

import engine.Engine;
import network.CommandLine;

import java.awt.Graphics;
import java.awt.Color;

public class CommandTextZone extends UIElement{

    public boolean isSelected;
    private final CommandLine commandLine;
    private final int maxChars = 40;
    private String text;
    private int textLen;
    private int charIndicator;

    public CommandTextZone(int posX, int posY, int scaleX, int scaleY, Engine parentEngine, CommandLine commandLine){
        super(posX, posY, scaleX, scaleY, null, parentEngine);
        this.charIndicator = maxChars;
        this.commandLine = commandLine;
    }

    public String getText() {
        return text;
    }

    private boolean isTextValid(){
        if(text == null)
            return false;
        return (textLen < maxChars);
    }

    private void updateText(String text){
        this.text = text;
        this.textLen = text.length();
        this.charIndicator = maxChars - textLen;
    }

    public void close(){
        getKeyManager().resetText();
        isSelected = false;
        commandLine.setVisible(false);
        Button.lock = false;
        getKeyManager().canType = true;
    }

    @Override
    public void tick() {

        if(isMouseOver && getMouseManager().isLeftPressed() && !isSelected){
            isSelected = true;
            commandLine.setVisible(true);
            Button.lock = true;
        }

        if(!isMouseOver && getMouseManager().isLeftPressed() && isSelected)
            if(isTextValid()){
                close();
            }

        if(isSelected){
            getKeyManager().canType = textLen<maxChars;
            updateText(getKeyManager().text.toString());

            if(getKeyManager().keyESC){
                close();
                updateText("");
            }

            if(getKeyManager().keyENTER){
                if(isTextValid()){
                    close();
                    commandLine.sendMessageToServer(text);
                    updateText("");
                }
            }

        }
        else{
            getKeyManager().canType = false;
        }
    }

    @Override
    public void render(Graphics g) {

        int resultX = posX-(scaleX/2);
        int resultY = posY-(scaleY/2);

        int selectPadding = 2;
        g.setColor(Color.white);
        if(isSelected){
            if(getMouseManager().isLeftPressed() && !isMouseOver)
                g.setColor(Color.red);
            g.fillRect(resultX-selectPadding, resultY-selectPadding, scaleX+selectPadding*2, scaleY+selectPadding*2);
        }

        if(isSelected){
            g.setColor(Color.darkGray);
            g.drawString(String.valueOf(charIndicator), posX+(int)(scaleX*0.45f), posY + (scaleY));
        }
        else{
            g.setColor(new Color(50,50,50,100));
        }
        g.fillRect(resultX, resultY, scaleX, scaleY);

        Color textColor = Color.white;
        if(!isTextValid())
            textColor = Color.red;

        if(textLen == 0){
            text = "Type here...";
            textColor = Color.gray;
        }
        g.setColor(textColor);
        g.drawString(text, posX-(int)(scaleX*0.45), posY);
    }
}
