package object.interactive.computer;

import ui.Button;

import java.awt.Graphics;

public abstract class File {

    private Button button;

    protected final Computer parentComputer;
    protected final Folder parent;
    protected final String name;

    public File(String name, Folder parent, Computer parentComputer){
        this.name = name;
        this.parent = parent;
        this.parentComputer = parentComputer;
    }

    public String getName() {
        return name;
    }

    public Folder getParent() {
        return parent;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public void loadContent(){}

    public void clearContent(){}

    public void tick(){}

    public void render(Graphics g){}
}
