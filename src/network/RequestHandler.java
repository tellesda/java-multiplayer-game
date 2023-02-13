package network;

import network.packetClasses.*;
import object.interactive.Door;
import scene.World;
import java.awt.Color;

//This class translates network requests into game actions
public class RequestHandler {

    private final World world;

    public RequestHandler(World world){
        this.world = world;
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

}
