package network.packetClasses;

public class PlayerMovementPacket extends Packet{

    public byte id;
    public byte animation;
    public short x,y; //divide by 100 for float conversion
}
