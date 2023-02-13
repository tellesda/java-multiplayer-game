package network;

import network.packetClasses.MessagePacket;
import scene.World;
import ui.CommandTextHistory;
import ui.CommandTextZone;
import ui.TextBubble;

import java.awt.Graphics;
import java.awt.Color;

public class CommandLine{

    private final CommandTextZone commandTextZone;
    private final CommandTextHistory commandTextHistory;
    private boolean isVisible;
    private final World world;

    public CommandLine(World world){
        this.commandTextZone = new CommandTextZone(200, world.getParentEngine().height-40, 350, 30, world.getParentEngine(), this);
        this.commandTextHistory = new CommandTextHistory(200, world.getParentEngine().height-200,350,250, world.getParentEngine());
        this.world = world;
    }

    public CommandTextZone getCommandTextZone() {
        return commandTextZone;
    }

    public void printCommandLine(String message, Color color){
        TextBubble textBubble = new TextBubble(message, color);
        commandTextHistory.addElement(textBubble);
    }

    public void sendMessageToServer(String message){
        GameClient gameClient = world.getParentEngine().gameClient;

        if(gameClient.getClient().isConnected()){
            MessagePacket messagePacket = new MessagePacket();
            messagePacket.message = gameClient.name + ": " + message;
            gameClient.getClient().sendTCP(messagePacket);
        }
        else{
            printCommandLine("You are not connected to a server.", Color.white);
        }

    }

    public World getWorld() {
        return world;
    }

    public void adjustResolution(int screenHeight){
        commandTextHistory.setPosY(screenHeight-200);
        commandTextZone.setPosY(screenHeight-40);
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public void init(){
        adjustResolution(world.getParentEngine().height);
    }

    public void tick(){
        commandTextZone.tick();
    }

    public void render(Graphics g){
        commandTextZone.render(g);
        commandTextHistory.render(g, isVisible);
    }


}
