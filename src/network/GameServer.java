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

    private final Engine engine;
    private final Server server;
    private GameServerListener serverListener;

    private final String serverName;
    private final int maxPlayers;
    private final List<GameConnection> pendingConnections;
    private final List<GameConnection> gameConnections;

    public GameServer(Engine engine, ServerInfo serverInfo){

        this.engine = engine;
        this.maxPlayers = serverInfo.maxPlayers;
        this.serverName = serverInfo.serverName;

        this.gameConnections = new ArrayList<>();
        this.pendingConnections = new ArrayList<>();
        this.server = new Server();

        //Register packets
        engine.getRequestHandler().registerPackets(server);

        startServer();
    }

    public Server getServer() {
        return server;
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
    public Engine getEngine() {
        return engine;
    }

    public boolean isServerFull(){
        return (gameConnections.size() >= maxPlayers);
    }

    public void startServer(){
        try {

            //Start the server
            server.bind(engine.port,engine.port);
            server.start();
            if(serverListener == null){
                GameServerListener listener = new GameServerListener(this);
                server.addListener(listener);
                this.serverListener = listener;
            }
            System.out.println("Server is operational");
            System.out.println("name: " + serverName);
            System.out.println("max players: " + maxPlayers);




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

    public void echoAll(String message){
        MessagePacket packet = new MessagePacket();
        packet.message = message;
        System.out.println(packet.message);
        packet.isOrange = true;
        sendPacket(packet);
    }

    //Runs each time a player is validated by the server
    public void onPlayerValid(GameConnection gc){

        World world = engine.getRequestHandler().getWorld();

        //Add OtherPlayer to server
        OtherPlayerPacket playerInfo = new OtherPlayerPacket();
        playerInfo.id = gc.id;
        playerInfo.name = gc.name;
        playerInfo.isAdding = true;
        world.addOtherPlayer(playerInfo);
        sendPacket(playerInfo); //Request all existing players to add the new player
        engine.getRequestHandler().syncClient(gc); //Request the new player to sync with the server

        echoAll(gc.name + " has joined the game!");
    }


    public void sendMovementInfo(){
        World world = engine.getRequestHandler().getWorld();
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
        World world = engine.getRequestHandler().getWorld();
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
