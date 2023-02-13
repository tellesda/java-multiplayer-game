package network;

import com.esotericsoftware.kryonet.Connection;

public class GameConnection {

    public final Connection c;
    public String name;
    public byte id;
    public String adress;

    public GameConnection(Connection c){
        this.c = c;
    }
}
