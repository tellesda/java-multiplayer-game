package scene;

import anim.Assets;
import engine.Engine;
import object.interactive.computer.Computer;
import object.Player;
import ui.Button;
import ui.UIElement;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ComputerScene implements Scene{

    //Attributes
    private final Computer computer;
    private final Player player;
    private final BufferedImage texture;
    private final Engine parentEngine;
    private final int windowPosX, windowPosY, windowWidth;
    private final List<UIElement> uiElements;

    private Button returnButton;
    private Button exitButton;

    public ComputerScene(Player player, Computer computer){
        this.player = player;
        Engine parentEngine = player.getWorld().getParentEngine();

        this.parentEngine = parentEngine;
        this.uiElements = new ArrayList<>();
        this.windowWidth = (int)(parentEngine.width*0.5);
        this.windowPosX = parentEngine.width/2 - windowWidth/2;
        this.windowPosY = parentEngine.height/2 - windowWidth/2;
        this.texture = Assets.computerScene;
        this.computer = computer;
        init();
    }

    public int getWindowPosX() {
        return windowPosX;
    }

    public int getWindowPosY() {
        return windowPosY;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public Engine getParentEngine() {
        return parentEngine;
    }

    public void exitScene(){
        computer.clear();
        this.player.stopInteraction();
    }

    public void init() {
        returnButton = new Button(null, Assets.backButton, windowPosX+(windowWidth/4),
                                                                windowPosY+(windowWidth/4),
                windowWidth/20, windowWidth/20, parentEngine);

        exitButton = new Button(null, Assets.deleteButton, windowPosX+windowWidth, windowPosY,
                windowWidth/10, windowWidth/10, parentEngine);

        uiElements.add(returnButton);
        uiElements.add(exitButton);
        computer.load(this);
    }

    public void tick() {
        for(var uiElement : uiElements)
            uiElement.tick();

        if(exitButton.isClicked()){
            exitScene();
        }
        if(returnButton.isClicked()){
            if(computer.getCurrentFile() != computer.getRoot())
                computer.setCurrentFile(computer.getCurrentFile().getParent());
        }

        computer.tick();
    }

    public void render(Graphics g) {
        int padding = 10;
        g.setColor(Color.darkGray);
        g.fillRect(windowPosX-padding, windowPosY-padding, windowWidth+(padding*2), windowWidth+(padding*2));
        g.drawImage(texture, windowPosX, windowPosY, windowWidth, windowWidth, null);

        for(var uiElement : uiElements)
            uiElement.render(g);

        computer.renderScreen(g);
    }
}
