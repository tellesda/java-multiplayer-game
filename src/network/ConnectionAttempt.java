package network;

import ui.ServerInfoUI;

import java.io.IOException;
import java.net.Socket;

public class ConnectionAttempt implements Runnable{

    ServerInfoUI server;

    public ConnectionAttempt(ServerInfoUI server){
        this.server = server;
    }

    public boolean canConnect(){
        try (Socket s = new Socket(server.getServerIp(), 8656)) {

            return true;
        } catch (IOException ex) {
            /* ignore */
        }
        return false;
    }

    @Override
    public void run() {
        if(canConnect())
            server.getConnectButton().setText("Online");
        else
            server.getConnectButton().setText("Offline");
    }

}
