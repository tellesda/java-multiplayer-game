package scene;

import anim.Assets;
import engine.Engine;
import ui.Button;
import ui.ServerSlot;
import ui.UIElement;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class MultiplayerTab implements Scene{

    private final Engine parentEngine;
    private final List<UIElement> uiElements;

    private Button menuButton;
    private Button hostGameButton;
    private ServerSlot serverSlot;
    private boolean isAttemptingConnection;
    private int connectionAttempts;

    public MultiplayerTab(Engine parentEngine){
        this.uiElements = new ArrayList<>();
        this.parentEngine = parentEngine;
    }

    public void init() {
        int centerX = parentEngine.width/2;
        int centerY = parentEngine.height/2;
        int downLeftX = 50;
        int downLeftY = parentEngine.height-50;

        menuButton = new Button(null, Assets.backButton, downLeftX, downLeftY, 50, 50, parentEngine);

        hostGameButton = new Button("Host game", null, centerX, 50, 200, 50, parentEngine);
        serverSlot = new ServerSlot(centerX, centerY, (int)(parentEngine.width*0.75), 400, parentEngine);
        uiElements.add(hostGameButton);
        uiElements.add(menuButton);
        uiElements.add(serverSlot);

    }

    public void tick() {

        if(isAttemptingConnection){
            if(!parentEngine.gameClient.getClient().isConnected()){
                parentEngine.gameClient.connect("localhost",8656);
                connectionAttempts++;

                if(connectionAttempts > 5){
                    isAttemptingConnection = false;
                    connectionAttempts = 0;
                }

            }
        }

        for(var uiElement : uiElements)
            uiElement.tick();

        if(menuButton.isClicked()){
            parentEngine.setCurrentScene(new MainMenu(parentEngine));
        }
        if(hostGameButton.isClicked()){
            if(parentEngine.hostedServer == null){
                parentEngine.hostedServer = new Engine(800,600,60,true);
                parentEngine.hostedServer.start();
                isAttemptingConnection = true;
            }
        }
    }

    public void render(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0,0, parentEngine.width, parentEngine.height);
        for(var uiElement : uiElements)
            uiElement.render(g);
    }
}