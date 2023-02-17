package network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import network.packetClasses.*;
import object.interactive.Door;
import scene.World;
import java.awt.Color;

//This class translates network requests into game actions
public class RequestHandler {

    private World world;

    public RequestHandler(){}

    public void setWorld(World world) {
        this.world = world;
    }

    public GameStatePacket getGameState(){
        GameStatePacket packet = new GameStatePacket();
        packet.level = (byte)world.getLevel().getCurrentLevel();

        return  packet;
    }

    public World getWorld() {
        return world;
    }

    public void registerPackets(Client client){
        client.getKryo().register(MessagePacket.class);
        client.getKryo().register(PlayerInfoPacket.class);
        client.getKryo().register(ServerInfoPacket.class);
        client.getKryo().register(PlayerMovementPacket.class);
        client.getKryo().register(OtherPlayerPacket.class);
        client.getKryo().register(DoorPacket.class);
        client.getKryo().register(GameStatePacket.class);
    }

    public void registerPackets(Server server){
        server.getKryo().register(MessagePacket.class);
        server.getKryo().register(PlayerInfoPacket.class);
        server.getKryo().register(ServerInfoPacket.class);
        server.getKryo().register(PlayerMovementPacket.class);
        server.getKryo().register(OtherPlayerPacket.class);
        server.getKryo().register(DoorPacket.class);
        server.getKryo().register(GameStatePacket.class);
    }

    public void processPacket(Packet packet){
        if(packet instanceof PlayerMovementPacket)
            processMovementInfo((PlayerMovementPacket) packet);
        else if(packet instanceof DoorPacket)
            processDoorInfo((DoorPacket) packet);
        else if(packet instanceof OtherPlayerPacket)
            processOtherPlayerInfo((OtherPlayerPacket) packet);
        else if(packet instanceof MessagePacket)
            processMessage((MessagePacket) packet);
        else if(packet instanceof GameStatePacket)
            processGameStateInfo((GameStatePacket) packet);


    }

    private void processDoorInfo(DoorPacket packet){
        Door door = null;
        for(var levelDoor : world.getLevel().getDoors()){
            if(levelDoor.getCode() == packet.doorCode){
                door = levelDoor;
                break;
            }
        }
        if(door == null)
            return;

        if(packet.open)
            door.open();
        else
            door.close();

        //Send back door info to clients
        if(world.getParentEngine().isServer){
            world.getParentEngine().gameServer.sendPacket(packet);
        }
    }

    private void processMovementInfo(PlayerMovementPacket packet){

        if(!world.getParentEngine().isServer){
            if(packet.id == world.getParentEngine().gameClient.id){
                return;
            }
        }
        world.setOtherPlayerLocation(packet);
    }

    private void processOtherPlayerInfo(OtherPlayerPacket packet){

        if(packet.isAdding){

            if(!world.getParentEngine().isServer){
                if(packet.id == world.getParentEngine().gameClient.id){
                    return;
                }
            }

            world.addOtherPlayer(packet);
        }
        else{
            world.removeOtherPlayer(packet.id);
        }
    }

    private void processMessage(MessagePacket packet){

        if(world.getParentEngine().isServer){
            packet.isOrange = false;
            world.getParentEngine().gameServer.sendPacket(packet);
            System.out.println(packet.message);
            return;
        }

        Color messageColor = Color.white;
        if(packet.isOrange)
            messageColor = Color.orange;

        world.getCommandLine().printCommandLine(packet.message, messageColor);
    }

    private void processGameStateInfo(GameStatePacket packet){
        if(world.getParentEngine().isServer)
            return;

        if(packet.level != world.getLevel().getCurrentLevel()){
            world.loadLevel(packet.level);
        }
    }


    //Exclusive to server
    public void syncClient(GameConnection gc){

        if(!world.getParentEngine().isServer)
            return;

        //Sync gameState
        gc.c.sendTCP(getGameState());

        //Sync doors
        for(var door : world.getLevel().getDoors()){
            DoorPacket packet = new DoorPacket();
            packet.doorCode = (byte)door.getCode();
            packet.open = door.isOpen();
            gc.c.sendTCP(packet);
        }

        //Sync OtherPlayers
        for(var otherPlayer : world.getOtherPlayers()){
            OtherPlayerPacket packet = new OtherPlayerPacket();
            packet.isAdding = true;
            packet.id = (byte)otherPlayer.id;
            packet.name = otherPlayer.name;
            gc.c.sendTCP(packet);
        }
    }

}
