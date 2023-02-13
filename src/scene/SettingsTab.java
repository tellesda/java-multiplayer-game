package scene;

import anim.Assets;
import engine.Engine;
import ui.Button;
import ui.UIElement;
import java.awt.Graphics;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SettingsTab implements Scene{

    private final Engine parentEngine;
    private final List<UIElement> uiElements;

    private int currentWidth;
    private int currentHeight;
    private int currentFPS;

    private Button menuButton;
    private Button res1;
    private Button res2;
    private Button res3;

    private Button fps30;
    private Button fps60;
    private Button fps120;
    private Button fps144;

    public SettingsTab(Engine parentEngine){
        this.uiElements = new ArrayList<>();
        this.parentEngine = parentEngine;
        currentWidth = parentEngine.width;
        currentHeight = parentEngine.height;
        currentFPS = parentEngine.fps;
    }

    private void saveSettings(){
        File serverFile = new File("save/settings.txt");
        try {
            PrintWriter pr = new PrintWriter(serverFile);
            pr.println("r " + currentWidth + " " + currentHeight);
            pr.println("f " + currentFPS);
            pr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] loadSettings(){
        try {
            int[] settings = new int[3];
            File serverFile = new File("save/settings.txt");
            Scanner myReader = new Scanner(serverFile);

            while (myReader.hasNextLine()) {
                String[] data = myReader.nextLine().split(" ");
                String label = data[0];
                if(label.equals("r")){
                    settings[0] = Integer.parseInt(data[1]);
                    settings[1] = Integer.parseInt(data[2]);
                }
                if(label.equals("f")){
                    settings[2] = Integer.parseInt(data[1]);
                }
            }
            myReader.close();
            return settings;

        } catch (FileNotFoundException ignored) {

        }
        return null;
    }

    public void init() {
        int centerX = parentEngine.width/2;
        int centerY = parentEngine.height/2;
        int downLeftX = 50;
        int downLeftY = parentEngine.height-50;

        menuButton = new Button(null, Assets.backButton, downLeftX, downLeftY, 50, 50, parentEngine);

        res3 = new Button("1920x1080", null, centerX-100, centerY+50, 100, 25, parentEngine);
        res2 = new Button("1280x720", null, centerX-100, centerY, 100, 25, parentEngine);
        res1 = new Button("800x600", null, centerX-100, centerY-50, 100, 25, parentEngine);

        fps144 = new Button("144fps", null, centerX+100, centerY+100, 100, 25, parentEngine);
        fps120 = new Button("120fps", null, centerX+100, centerY+50, 100, 25, parentEngine);
        fps60 = new Button("60fps", null, centerX+100, centerY, 100, 25, parentEngine);
        fps30 = new Button("30fps", null, centerX+100, centerY-50, 100, 25, parentEngine);

        uiElements.add(menuButton);

        uiElements.add(res3);
        uiElements.add(res2);
        uiElements.add(res1);

        uiElements.add(fps144);
        uiElements.add(fps120);
        uiElements.add(fps60);
        uiElements.add(fps30);
    }

    public void tick() {
        for(var uiElement : uiElements)
            uiElement.tick();

        if(menuButton.isClicked()){
            saveSettings();
            parentEngine.setCurrentScene(new MainMenu(parentEngine));
        }
        if(fps30.isClicked()){
            parentEngine.fps = 30;
            currentFPS = 30;
        }
        if(fps60.isClicked()){
            parentEngine.fps = 60;
            currentFPS = 60;
        }
        if(fps120.isClicked()){
            parentEngine.fps = 120;
            currentFPS = 120;
        }

        if(fps144.isClicked()){
            parentEngine.fps = 144;
            currentFPS = 144;
        }

        if(res3.isClicked()){
            parentEngine.setResolution(1920,1080);
            currentWidth = 1920;
            currentHeight = 1080;
        }
        if(res2.isClicked()){
            parentEngine.setResolution(1280,720);
            currentWidth = 1280;
            currentHeight = 720;
        }
        if(res1.isClicked()){
            parentEngine.setResolution(800,600);
            currentWidth = 800;
            currentHeight = 600;
        }


    }

    public void render(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0,0, parentEngine.width, parentEngine.height);
        for(var uiElement : uiElements)
            uiElement.render(g);
    }
}
