package network.packetClasses;

public class ServerInfoPacket extends Packet{
    
    public byte playerID; //ID assigned to the client by the server
    public String serverName;
}
