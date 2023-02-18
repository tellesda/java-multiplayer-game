package ui;

import anim.Assets;
import engine.Engine;
import network.ConnectionAttempt;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class ServerInfoUI extends UIElement{

    private final ServerSlot parent;
    private final String serverIp;
    private final Button connectButton;
    private final Button removeButton;
    private BufferedImage serverImage;

    public ServerInfoUI(String ip, int posX, int posY, int scaleX, int scaleY, Engine parentEngine, ServerSlot parent){
        super(posX, posY, scaleX, scaleY, Assets.button, parentEngine);
        this.parent = parent;
        this.serverIp = ip;
        connectButton = new Button("Connect", null, posX+(scaleX/2)-100, posY, 100, 25, parentEngine);
        removeButton = new Button(null, Assets.deleteButton, posX+(scaleX/2)-30, posY, 25, 25, parentEngine);
        this.serverImage = Assets.serverOff;
        checkStatus();
    }

    public String getServerIp() {
        return serverIp;
    }

    public Button getConnectButton() {
        return connectButton;
    }

    public void setServerImage(BufferedImage serverImage) {
        this.serverImage = serverImage;
    }

    public void updateY(int posY){
        this.posY = posY;
        connectButton.posY = posY;
        removeButton.posY = posY;
    }

    public void checkStatus(){
        getConnectButton().setText("Checking...");
        Thread th = new Thread(new ConnectionAttempt(this));
        th.start();
    }

    @Override
    public void tick() {
        connectButton.tick();
        removeButton.tick();
        if(connectButton.isClicked()){
            if(getConnectButton().getText().equals("Online")){
                if(!parentEngine.gameClient.getClient().isConnected())
                    parentEngine.gameClient.connect(serverIp, parentEngine.port);
            }
        }
        if(removeButton.isClicked()){
            parent.removeServer(this);
        }
    }

    @Override
    public void render(Graphics g) {
        int stringX = posX-(scaleX/2);
        int stringY = posY-(scaleY/2);
        int imgX = posX+(scaleX/2);
        g.setColor(Color.DARK_GRAY);
        g.fillRoundRect(stringX, stringY, scaleX, scaleY, 5, 5);

        g.setColor(new Color(200,200,200));
        g.drawString(serverIp, stringX+20, posY);
        g.drawImage(serverImage, imgX-200, stringY+5, (int)(scaleY*0.7f*0.8f), (int)(scaleY*0.8f), null);

        connectButton.render(g);
        removeButton.render(g);
    }
}
