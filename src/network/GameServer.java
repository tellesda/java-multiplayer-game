package network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import engine.Engine;
import network.listeners.GameServerListener;
import network.packetClasses.*;
import scene.World;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameServer {

    private final int port = 8656;
    private World world;
    private Server server;
    private GameServerListener serverListener;

    private String serverName;
    private int maxPlayers;
    private List<GameConnection> pendingConnections;
    private List<GameConnection> gameConnections;

    public GameServer(World game, int maxPlayers, String serverName){
        if(game == null)
            return;

        this.maxPlayers = maxPlayers;
        this.serverName = serverName;

        this.gameConnections = new ArrayList<>();
        this.pendingConnections = new ArrayList<>();

        this.world = game;
        this.server = new Server();

        //Register packets
        server.getKryo().register(MessagePacket.class);
        server.getKryo().register(PlayerInfoPacket.class);
        server.getKryo().register(ServerInfoPacket.class);
        server.getKryo().register(PlayerMovementPacket.class);
        server.getKryo().register(OtherPlayerPacket.class);
        server.getKryo().register(DoorPacket.class);
        server.getKryo().register(GameStatePacket.class);

        startServer();
    }

    public Server getServer() {
        return server;
    }
    public World getWorld() {
        return world;
    }
    public int getMaxPlayers() {
        return maxPlayers;
    }
    public String getServerName() {
        return serverName;
    }
    public List<GameConnection> getGameConnections() {
        return gameConnections;
    }
    public List<GameConnection> getPendingConnections() {
        return pendingConnections;
    }

    public boolean isServerFull(){
        return (gameConnections.size() >= maxPlayers);
    }

    public void startServer(){
        try {

            //Start the server
            server.bind(port,port);
            server.start();
            if(serverListener == null){
                GameServerListener listener = new GameServerListener(this);
                server.addListener(listener);
                this.serverListener = listener;
            }
            System.out.println("Server is operational");


        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while starting the server.");
        }
    }

    public void stopServer(){
        server.stop();
        server.removeListener(serverListener);
        this.serverListener = null;
    }

    public void validateConnection(PlayerInfoPacket packet, Connection c){
        GameConnection gc = null;

        for(var pendingConnection : pendingConnections){
            if(pendingConnection.adress.equals(c.getRemoteAddressTCP().getHostString())){
                gc = pendingConnection;
            }
        }

        if(gc == null){
            return;
        }

        packet.id = getAvailableId();
        gc.name = packet.playerName;
        gc.id = packet.id;

        pendingConnections.remove(gc);

        for(var gameConnection : gameConnections){
            if(gameConnection.name.equals(gc.name)){
                System.out.println("Connection refused: A user with the same name already exists");
                return;
            }
        }

        if(isServerFull()){
            System.out.println("Connection refused: Max number of players reached");
            return;
        }

        gameConnections.add(gc);
        onPlayerValid(gc);

    }

    //Runs each time a player is validated by the server
    public void onPlayerValid(GameConnection gc){

        //Add OtherPlayer to server
        OtherPlayerPacket playerInfo = new OtherPlayerPacket();
        playerInfo.id = gc.id;
        playerInfo.name = gc.name;
        playerInfo.isAdding = true;
        world.addOtherPlayer(playerInfo);
        sendPacket(playerInfo); //Request all existing players to add the new player
        world.getRequestHandler().syncClient(gc); //Request the new player to sync with the server

        MessagePacket packet = new MessagePacket();
        packet.message = gc.name + " (ID: " + gc.id + ")" + " has joined the game!";
        System.out.println(packet.message);
        packet.isOrange = true;
        sendPacket(packet);
    }


    public void sendMovementInfo(){
        for(var gameConnection : gameConnections){
            if(gameConnection.id == -1)
                continue;
            for(var otherPlayer : world.getOtherPlayers()){
                if(gameConnection.id == otherPlayer.id)
                    continue;
                PlayerMovementPacket packet = new PlayerMovementPacket();
                packet.x = Engine.floatToShort(otherPlayer.getLocation().getX());
                packet.y = Engine.floatToShort(otherPlayer.getLocation().getY());
                packet.animation = otherPlayer.getAnimationIndex();
                packet.id = (byte)otherPlayer.id;

                gameConnection.c.sendUDP(packet);
            }
        }
    }


    public void sendPacket(Packet packet){
        for(var gameConnection : gameConnections){
            gameConnection.c.sendTCP(packet);
        }
    }


    public void removeOtherPlayer(int id){
        world.removeOtherPlayer(id);
        OtherPlayerPacket packet = new OtherPlayerPacket();
        packet.id = (byte)id;
        packet.isAdding = false;
        sendPacket(packet);
    }


    public byte getAvailableId(){
        boolean[] takenIDs = new boolean[128];
        for(var gameConnection : gameConnections){
            takenIDs[gameConnection.id] = true;
        }
        for(int id = 0; id < takenIDs.length; id++){
            if(!takenIDs[id])
                return (byte)id;
        }
       return -1;
    }


}
