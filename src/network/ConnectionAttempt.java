package network;

import anim.Assets;
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
        server.setServerImage(Assets.serverCheck);
        if(canConnect()) {
            server.getConnectButton().setText("Online");
            server.setServerImage(Assets.serverOn);
        } else {
            server.getConnectButton().setText("Offline");
            server.setServerImage(Assets.serverOff);
        }
    }

}
