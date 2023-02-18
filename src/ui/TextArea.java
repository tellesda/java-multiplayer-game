package ui;

import anim.Assets;
import engine.Engine;

import java.awt.Graphics;
import java.awt.Color;

public class TextArea extends UIElement{

    public boolean isSelected;
    private final int minChars;
    private final int maxChars;
    private final boolean numOnly;
    private String text;
    private final String title;
    private final String emptyMessage;
    private final String defaultText;
    private int textLen;

    public TextArea(boolean numOnly, int minChars, int maxChars, int posX, int posY, int scaleX, int scaleY,
                    Engine parentEngine, String defaultText, String emptyMessage, String title){
        super(posX, posY, scaleX, scaleY, Assets.textBar, parentEngine);
        this.numOnly = numOnly;
        this.minChars = minChars;
        this.maxChars = maxChars;
        this.defaultText = defaultText;
        this.emptyMessage = emptyMessage;
        this.title = title;
        updateText(defaultText);
    }

    public String getText() {
        return text;
    }

    public int getValue(){
        return Integer.parseInt(text);
    }

    private boolean isTextValid(){
        if(text == null)
            return false;

        if(numOnly){
            try{
                Integer.parseInt(text);
            }
            catch (NumberFormatException e){
                return false;
            }
        }

        return (textLen <= maxChars && textLen >= minChars);
    }

    private void updateText(String text){
        this.text = text;
        this.textLen = text.length();
    }

    @Override
    public void tick() {
        super.tick();

        if(isMouseOver && getMouseManager().isLeftPressed() && !isSelected){
            isSelected = true;
            Button.lock = true;
        }

        if(!isSelected)
            return;

        if(!isMouseOver && getMouseManager().isLeftPressed() && isSelected)
            if(isTextValid()){
                getKeyManager().resetText();
                isSelected = false;
                Button.lock = false;
            }

        if(isSelected){
            getKeyManager().canType = textLen<maxChars;
            updateText(getKeyManager().text.toString());

            if(getKeyManager().keyESC){
                getKeyManager().resetText();
                isSelected = false;
                Button.lock = false;
                updateText(defaultText);
            }

            if(getKeyManager().keyENTER){
                if(isTextValid()){
                    getKeyManager().resetText();
                    isSelected = false;
                    Button.lock = false;
                }
            }

        }
        else{
            getKeyManager().canType = false;
        }
    }

    @Override
    public void render(Graphics g) {

        int renderScaleX = scaleX;
        int renderScaleY = scaleY;

        if(isMouseOver){
            renderScaleX *= 1.08;
            renderScaleY *= 1.08;
        }

        int resultX = posX-(renderScaleX/2);
        int resultY = posY-(renderScaleY/2);

        int selectPadding = 2;
        g.setColor(Color.white);
        if(isSelected){
            if(getMouseManager().isLeftPressed() && !isMouseOver)
                g.setColor(Color.red);
            g.fillRect(resultX-selectPadding, resultY-selectPadding, renderScaleX+selectPadding*2, renderScaleY+selectPadding*2);
        }

        g.drawImage(texture, resultX, resultY, renderScaleX, renderScaleY, null);

        if(text != null){
            Color textColor = Color.white;
            if(!isTextValid())
                textColor = Color.red;

            g.setColor(textColor);

            if(textLen == 0){ //Draw empty message if the text is empty
                g.setColor(Color.gray);
                text = emptyMessage;
            }
            int stringWidth = g.getFontMetrics().stringWidth(text);
            g.drawString(text, posX-(stringWidth/2), posY);

            //Render title
            g.setColor(Color.white);
            stringWidth = g.getFontMetrics().stringWidth(title);
            g.drawString(title, posX-(stringWidth/2), posY-(scaleY+16)/2);
        }
    }
}
