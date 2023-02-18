package ui;

import anim.Assets;
import engine.Engine;

import java.awt.Graphics;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Scanner;

public class ServerSlot extends UIElement{

    private final Engine parentEngine;
    private final int numberOfSlots = 4;
    private final int padding = 60;
    private List<ServerInfoUI> servers;

    private final Button addButton;
    private final TextArea ipArea;

    public ServerSlot(int posX, int posY, int scaleX, int scaleY, Engine parentEngine){
        super(posX, posY, scaleX, scaleY, Assets.button, parentEngine);
        this.parentEngine = parentEngine;
        this.servers = new ArrayList<>(numberOfSlots);
        this.addButton = new Button("Add server", null, posX-110, posY+(scaleY/2)-60, 200, 50, parentEngine);
        this.ipArea = new TextArea(false, 1,15,posX+110,posY+(scaleY/2)-60, 200, 50,
                parentEngine, "127.0.0.1", "Type an ip address...", "ip address");
        loadServerList();
    }

    public Engine getParentEngine() {
        return parentEngine;
    }

    private void saveServerList(){
        File serverFile = new File("save/servers/servers.txt");
        try {
            PrintWriter pr = new PrintWriter(serverFile);
            for(var server : servers)
                pr.println(server.getServerIp());
            pr.close();
        } catch (IOException ignored) {
        }
    }

    public void loadServerList(){
        try {
            File serverFile = new File("save/servers/servers.txt");
            Scanner myReader = new Scanner(serverFile);

            servers = new ArrayList<>(numberOfSlots);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                addServer(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addServer(String ip){
        if(servers.size()==numberOfSlots){
            parentEngine.logMessage("Maximum number of server reached!");
            return;
        }
        int resultY = posY+((servers.size()-(numberOfSlots/2))*padding);
        servers.add(new ServerInfoUI(ip, posX, resultY, (int)(scaleX*0.9f), 50, parentEngine, this));
        saveServerList();
    }

    public void removeServer(ServerInfoUI serverInfo){
        servers.remove(serverInfo);
        for(int i=0; i<servers.size(); i++){
            ServerInfoUI server = servers.get(i);
            int newPosY = posY+((i-(numberOfSlots/2))*padding);
            server.updateY(newPosY);
        }
        saveServerList();
    }

    @Override
    public void tick() {
        addButton.tick();
        ipArea.tick();
        try {
            for(var server : servers)
                server.tick();
        }
        catch (ConcurrentModificationException ignored){

        }
        if(addButton.isClicked()){
            addServer(ipArea.getText());
        }
    }

    @Override
    public void render(Graphics g) {
        int resultX = posX-(scaleX/2);
        int resultY = posY-(scaleY/2);
        g.setColor(Color.GRAY);
        g.fillRoundRect(resultX,resultY,scaleX,scaleY, 10, 10);
        for (ServerInfoUI server : servers) {
            server.render(g);
        }
        addButton.render(g);
        ipArea.render(g);
    }
}
