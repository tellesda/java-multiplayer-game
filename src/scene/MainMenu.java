package scene;

import anim.Assets;
import engine.Engine;
import ui.Button;
import ui.PopUp;
import ui.UIElement;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class MainMenu implements Scene {

    private final Engine parentEngine;
    private final List<UIElement> uiElements;

    private Button singleplayerButton;
    private Button multiplayerButton;
    private Button quitButton;
    private Button settingsButton;
    private Button customizeButton;

    public MainMenu(Engine parentEngine){
        this.uiElements = new ArrayList<>();
        this.parentEngine = parentEngine;
    }

    public Engine getParentEngine() {
        return parentEngine;
    }

    public void init(){
        int centerX = parentEngine.width/2;
        int centerY = parentEngine.height/2;
        int downRightX = parentEngine.width-50;
        int downRightY = parentEngine.height-50;

        singleplayerButton = new Button("Singleplayer", null, centerX,centerY-80,200,50, parentEngine);
        multiplayerButton = new Button("Multiplayer", null, centerX,centerY,200,50, parentEngine);
        customizeButton = new Button("Customize character", null, centerX,centerY+80,200,50, parentEngine);
        quitButton = new Button("Quit", null, centerX,centerY+160,200,50, parentEngine);
        settingsButton = new Button(null, Assets.settingsButton, downRightX,downRightY,50,50, parentEngine);

        uiElements.add(singleplayerButton);
        uiElements.add(multiplayerButton);
        uiElements.add(customizeButton);
        uiElements.add(quitButton);
        uiElements.add(settingsButton);
    }

    public void tick(){
        for(var uiElement : uiElements)
            uiElement.tick();

        if(singleplayerButton.isClicked()){
            parentEngine.setCurrentScene(new World(parentEngine));
        }

        if(multiplayerButton.isClicked()){
            parentEngine.setCurrentScene(new MultiplayerTab(parentEngine));
        }

        if(customizeButton.isClicked()){
            parentEngine.setCurrentScene(new CustomizationTab(parentEngine));
        }

        if(settingsButton.isClicked()){
            parentEngine.setCurrentScene(new SettingsTab(parentEngine));
        }

        if(quitButton.isClicked())
            System.exit(0);
    }

    public void render(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0,0, parentEngine.width, parentEngine.height);
        for(var uiElement : uiElements)
            uiElement.render(g);
    }
}
