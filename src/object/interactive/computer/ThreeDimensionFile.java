package object.interactive.computer;

import engine.Engine;
import object.interactive.computer.engine3d.objectClasses.World;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ThreeDimensionFile extends File{

    private BufferedImage gameScreen;
    private Graphics2D graphics2D;
    private Engine parentEngine;
    private final int windowSize = 64;

    private World world;

    public ThreeDimensionFile(String name, Folder parent, Computer parentComputer, Engine engine){
        super(name, parent, parentComputer);
        this.parentEngine = engine;
    }

    public void loadContent(){
        this.gameScreen = new BufferedImage(windowSize, windowSize, BufferedImage.TYPE_INT_RGB);
        this.graphics2D = gameScreen.createGraphics();
        this.world = new World("res/computers/3d/" + getName(), parentEngine);
        world.init();
    }

    public void clearContent(){
        this.gameScreen = null;
        this.graphics2D = null;
        this.world = null;
    }

    public void tick() {
        world.tick();
        world.render(graphics2D, windowSize, windowSize);
    }

    public void render(Graphics g){
        g.setColor(parentComputer.getTextColor());
        //Draw game padding
        //int scale = (int)(getButton().scaleX*1.04);
        //g.fillRect(parentComputer.getRoot().getButton().posX-(int)(getButton().scaleX*0.338),
        //        parentComputer.getRoot().getButton().posY-(int)(getButton().scaleX*0.17),
        //        scale,
        //        scale);

        //Draw game
        g.drawImage(gameScreen, parentComputer.getRoot().getButton().posX-(int)(getButton().scaleX*0.32),
                parentComputer.getRoot().getButton().posY-(int)(getButton().scaleX*0.15),
                getButton().scaleX,
                getButton().scaleX, null);
    }
}
