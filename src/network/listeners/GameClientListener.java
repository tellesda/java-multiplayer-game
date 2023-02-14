package network.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import network.GameClient;
import network.packetClasses.*;

public class GameClientListener extends Listener {

    private final GameClient client;

    public GameClientListener(GameClient client){
        this.client = client;
    }

    public void connected(Connection connection) {
        System.out.println("Connected to the server.");
    }

    //Runs when we receive a package
    public void received(Connection c, Object p){
        if(p instanceof Packet){

            if(p instanceof ServerInfoPacket packet){
                client.identifyPlayer(c);
                client.identifyServer(packet);
                return;
            }
            client.getWorld().getRequestHandler().processPacket((Packet) p);
        }
    }

    public void disconnected(Connection connection) {
        System.out.println("Connection from the server lost.");
        client.id = -1;
    }
}
