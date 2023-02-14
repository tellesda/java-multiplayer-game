package network.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import network.GameConnection;
import network.GameServer;
import network.packetClasses.*;

public class GameServerListener extends Listener {

    private final GameServer server;

    public GameServerListener(GameServer server){
        this.server = server;
    }

    //Runs when a connection is received
    public void connected(Connection c){
        if(server.isServerFull()){
            System.out.println("Max number of players reached");
            c.close();
            return;
        }
        for(var pendingConnection : server.getPendingConnections()){
            if(c.getRemoteAddressTCP().getHostString().equals(pendingConnection.adress)){
                System.out.println("Connection already pending.");
                c.close();
                return;
            }
        }

        GameConnection gc = new GameConnection(c);
        gc.adress = c.getRemoteAddressTCP().getHostString();
        server.getPendingConnections().add(gc);

        ServerInfoPacket packet = new ServerInfoPacket();
        packet.playerID = server.getAvailableId();
        packet.serverName = server.getServerName();
        c.sendTCP(packet);
    }

    //Runs when we receive a packet
    public void received(Connection c, Object p){

        if(p instanceof Packet){

            if(p instanceof PlayerInfoPacket packet){
                server.validateConnection(packet, c);
                return;
            }
            server.getWorld().getRequestHandler().processPacket((Packet) p);
        }
    }

    //Runs when a client disconnects
    public void disconnected(Connection c){

        GameConnection disconnectedPlayer = null;
        for(var player : server.getGameConnections()){
            if(!player.c.isConnected()){
                disconnectedPlayer = player;
            }
        }
        if(disconnectedPlayer == null)
            return;

        server.removeOtherPlayer(disconnectedPlayer.id);

        server.getGameConnections().remove(disconnectedPlayer);

        MessagePacket packet = new MessagePacket();
        packet.message = disconnectedPlayer.name + " has left the game.";
        System.out.println(packet.message);
        packet.isOrange = true;
        server.sendPacket(packet);
    }
}