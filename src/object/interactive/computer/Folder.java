package object.interactive.computer;

import ui.Button;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Folder extends File{

    private final List<File> files;

    public Folder(String name, Folder parent, Computer parentComputer){
        super(name, parent, parentComputer);
        this.files = new ArrayList<>();
    }

    public File getFileByName(String name){
        for(var file : files){
            if(file.getName().equals(name))
                return file;
        }
        return null;
    }

    public void addToFiles(File file, BufferedImage logo){
        this.files.add(file);
        Button rootButton = parentComputer.getRoot().getButton();
        file.setButton(new Button(file.getName(), logo,
                rootButton.posX, (int)(rootButton.posY+files.size()* rootButton.scaleY*1.2),
                rootButton.scaleX, rootButton.scaleY,
                rootButton.getParentEngine()));
        file.getButton().setAlignLeft(true);
    }

    public void tick(){
        for(var file : files){
            file.getButton().tick();
            if(file.getButton().isClicked())
                parentComputer.setCurrentFile(file);
        }
    }

    public void render(Graphics g){
        for(var file : files)
            file.getButton().render(g);
    }
}
