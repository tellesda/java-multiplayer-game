import engine.Engine;
import scene.SettingsTab;

public class Launcher {

    public static void main(String[] args) {
        int[] settings = SettingsTab.loadSettings();
        if(settings == null){
            settings = new int[]{1280,720,60}; //If the settings folder is not found
        }
        Engine engine = new Engine(settings[0], settings[1], settings[2], false);
        engine.start();
    }

}

