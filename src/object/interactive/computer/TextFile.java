package object.interactive.computer;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class TextFile extends File{

    private Stack<String> content;

    public TextFile(String name, Folder parent, Computer parentComputer){
        super(name, parent, parentComputer);
    }

    public void loadContent(){
        String path = "res/computers/textFiles/" + getName();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            this.content = new Stack<>();
            BufferedReader bf = new BufferedReader(new FileReader(path));
            String line;
            while ((line = bf.readLine()) != null){
                stringBuilder.append(line);
            }

            bf.close();

            //Split the full text
            int length = stringBuilder.length();
            int maxCharsPerLine = (int)(parentComputer.getRoot().getButton().scaleX*1.7)/8;
            for(int i=0; i<length; i += maxCharsPerLine){
                int end;
                String lastChar = "-";
                if(i + maxCharsPerLine >= length){
                    end = length-1;
                    lastChar = "";
                }
                else
                    end = i + maxCharsPerLine;

                content.add(stringBuilder.substring(i, end) + lastChar);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearContent(){
        this.content = null;
    }

    public void render(Graphics g) {
        g.setColor(parentComputer.getTextColor());
        int row = 0;

        for(var line : content){
            g.drawString(line,
                    getButton().posX-(int)(getButton().scaleX*0.5),
                    parentComputer.getRoot().getButton().posY+(int)(getButton().scaleX*0.15) + row* getButton().scaleY/2);
            row++;
        }
    }
}
