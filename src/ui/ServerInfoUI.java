package ui;

import anim.Assets;
import engine.Engine;

import java.awt.Graphics;
import java.awt.Color;

public class ServerInfoUI extends UIElement{

    private final ServerSlot parent;
    private final String serverIp;
    private final Button connectButton;
    private final Button removeButton;

    public ServerInfoUI(String ip, int posX, int posY, int scaleX, int scaleY, Engine parentEngine, ServerSlot parent){
        super(posX, posY, scaleX, scaleY, Assets.button, parentEngine);
        this.parent = parent;
        this.serverIp = ip;
        connectButton = new Button("Connect", null, posX+(scaleX/2)-100, posY, 100, 25, parentEngine);
        removeButton = new Button(null, Assets.deleteButton, posX+(scaleX/2)-30, posY, 25, 25, parentEngine);
    }

    public String getServerIp() {
        return serverIp;
    }

    public Button getConnectButton() {
        return connectButton;
    }

    public void updateY(int posY){
        this.posY = posY;
        connectButton.posY = posY;
        removeButton.posY = posY;
    }

    @Override
    public void tick() {
        connectButton.tick();
        removeButton.tick();
        if(connectButton.isClicked()){
            if(!parentEngine.gameClient.getClient().isConnected())
                parentEngine.gameClient.connect(serverIp, 8656);
        }
        if(removeButton.isClicked()){
            parent.removeServer(this);
        }
    }

    @Override
    public void render(Graphics g) {
        int resultX = posX-(scaleX/2);
        int resultY = posY-(scaleY/2);
        g.setColor(Color.DARK_GRAY);
        g.fillRoundRect(resultX, resultY, scaleX, scaleY, 5, 5);

        g.setColor(Color.WHITE);
        g.drawString(serverIp, resultX+20, posY);

        connectButton.render(g);
        removeButton.render(g);
    }
}
