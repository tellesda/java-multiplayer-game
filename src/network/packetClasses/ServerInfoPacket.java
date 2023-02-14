package network.packetClasses;

public class ServerInfoPacket extends Packet{
    
    public byte playerID;
    public String serverName;
    public GameStatePacket gameStatePacket;
}
