import engine.Engine;

public class ServerLauncher {

    public static void main(String[] args) {
        Engine engine = new Engine(800, 600, 60, true);
        engine.start();
    }
}
