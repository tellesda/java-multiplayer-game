package network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import engine.Engine;
import network.listeners.GameClientListener;
import network.packetClasses.*;
import scene.World;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class GameClient extends Listener {

    public String name;
    public int id;
    private final Client client;
    private GameClientListener clientListener;
    private World world;
    private final Engine engine;

    public GameClient(Engine engine){
        this.id = -1;
        this.engine = engine;
        this.name = loadName2();
        this.client = new Client();

        //Register packets
        client.getKryo().register(MessagePacket.class);
        client.getKryo().register(PlayerInfoPacket.class);
        client.getKryo().register(ServerInfoPacket.class);
        client.getKryo().register(PlayerMovementPacket.class);
        client.getKryo().register(OtherPlayerPacket.class);
        client.getKryo().register(DoorPacket.class);
        client.getKryo().register(GameStatePacket.class);
    }

    public World getWorld() {
        return world;
    }
    public void setWorld(World world) {
        this.world = world;
    }
    public Client getClient() {
        return client;
    }

    public void connect(String ip, int port){
        try {
            client.start();
            client.connect(5000, ip, port, port);
            if(clientListener == null){
                GameClientListener listener = new GameClientListener(this);
                this.clientListener = listener;
                client.addListener(listener);
            }
        } catch (IOException e) {
            System.out.println("Error while connecting to server.");
        }
    }

    public void disconnect(){
        if(this.clientListener != null){
            client.removeListener(this.clientListener);
            this.clientListener = null;
        }
        client.stop();
    }

    public void identifyPlayer(Connection c){
        PlayerInfoPacket packet = new PlayerInfoPacket();
        packet.playerName = name;
        c.sendTCP(packet);
    }

    public void sendMovementInfo(){
        if(id == -1){
            return;
        }
        PlayerMovementPacket packet = new PlayerMovementPacket();
        packet.id = (byte)id;
        packet.animation = world.getPlayer().getAnimationIndex();
        packet.x = Engine.floatToShort(world.getPlayer().getLocation().getX());
        packet.y = Engine.floatToShort(world.getPlayer().getLocation().getY());
        client.sendUDP(packet);
    }

    public void sendPacket(Packet packet){
        client.sendTCP(packet);
    }

    public void identifyServer(ServerInfoPacket packet){
        this.id = packet.playerID;
        engine.setCurrentScene(new World(engine));
        world.getRequestHandler().syncWithServer(packet.gameStatePacket);
    }

    public String loadName(){
        try {
            File serverFile = new File("save/customization.txt");
            Scanner myReader = new Scanner(serverFile);

            int line=0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(line == 0){
                    myReader.close();
                    return data;
                }
                line++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            return "player" + (int)(Math.random()*10000);
        }
        return null;
    }

    public String loadName2(){ //TODO remove
        return "player" + (int)(Math.random()*10000);
    }


}
