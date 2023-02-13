package ui;

import java.awt.Color;

public class TextBubble {

    private final String text;
    private final Color color;

    public TextBubble(String text, Color color){
        this.text = text;
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public Color getColor() {
        return color;
    }
}
