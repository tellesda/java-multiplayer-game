package object.interactive.computer;

import anim.ImageReader;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class ImageFile extends File{

    private BufferedImage content;

    public ImageFile(String name, Folder parent, Computer parentComputer){
        super(name, parent, parentComputer);
    }

    public void loadContent(){
        this.content = ImageReader.readImage("/res/computers/imgFiles/" + getName());
    }

    public void clearContent(){
        this.content = null;
    }

    public void render(Graphics g){
        g.drawImage(content, parentComputer.getRoot().getButton().posX-(int)(getButton().scaleX*0.32),
                             parentComputer.getRoot().getButton().posY-(int)(getButton().scaleX*0.15),
                parentComputer.getRoot().getButton().scaleX,
                parentComputer.getRoot().getButton().scaleX, null);
    }
}
