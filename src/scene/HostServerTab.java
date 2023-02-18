package scene;

import anim.Assets;
import engine.Engine;
import network.ServerInfo;
import ui.Button;
import ui.TextArea;
import ui.UIElement;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class HostServerTab implements Scene{

    private final Engine parentEngine;
    private final List<UIElement> uiElements;

    private Button menuButton;
    private Button hostGameButton;
    private TextArea serverName;
    private TextArea serverMaxPlayers;
    private boolean isAttemptingConnection;
    private int connectionAttempts;

    public HostServerTab(Engine parentEngine){
        this.uiElements = new ArrayList<>();
        this.parentEngine = parentEngine;
    }

    public void init() {
        int centerX = parentEngine.width/2;
        int centerY = parentEngine.height/2;
        int downLeftX = 50;
        int downLeftY = parentEngine.height-50;

        menuButton = new Button(null, Assets.backButton, downLeftX, downLeftY, 50, 50, parentEngine);

        hostGameButton = new Button("Host game", null, centerX, downLeftY, 200, 50, parentEngine);
        serverName = new TextArea(false, 3,24,centerX,centerY-50,400,50,
                parentEngine,parentEngine.gameClient.name+"'s server","Server name...", "Server name");
        serverMaxPlayers = new TextArea(true, 1,2,centerX,centerY+50, 100, 50,
                parentEngine, "4", "Max players...", "Max players");
        uiElements.add(hostGameButton);
        uiElements.add(menuButton);
        uiElements.add(serverMaxPlayers);
        uiElements.add(serverName);

    }

    public void tick() {

        if(isAttemptingConnection){
            if(!parentEngine.gameClient.getClient().isConnected()){
                parentEngine.gameClient.connect("localhost", parentEngine.port);
                connectionAttempts++;

                if(connectionAttempts > 5){
                    isAttemptingConnection = false;
                    connectionAttempts = 0;
                    //close server
                    parentEngine.logMessage("Error while creating the server! Please try again.");
                    parentEngine.closeServer();
                }

            }
        }

        for(var uiElement : uiElements)
            uiElement.tick();

        if(menuButton.isClicked()){
            parentEngine.setCurrentScene(new MultiplayerTab(parentEngine));
        }
        if(hostGameButton.isClicked()){
            if(!serverMaxPlayers.getText().equals("0") && !serverMaxPlayers.getText().equals("00")){
                if(parentEngine.hostedServer == null){

                    parentEngine.hostedServer = new Engine(800,600,60,
                            new ServerInfo(serverName.getText(), serverMaxPlayers.getValue()));
                    parentEngine.hostedServer.start();
                    isAttemptingConnection = true;
                }
            }
            else{
                parentEngine.logMessage("Please enter a valid number of players.");
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
