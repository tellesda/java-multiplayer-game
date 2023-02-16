import engine.Engine;
import network.ServerInfo;

public class ServerLauncher {

    public static void main(String[] args) {

        Engine engine = new Engine(800, 600, 60, new ServerInfo("myServer", 4));
        engine.start();
    }
}
