package scene;

import anim.Assets;
import engine.Engine;
import ui.Button;
import ui.TextArea;
import ui.UIElement;
import java.awt.Graphics;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CustomizationTab implements Scene{

    private final Engine parentEngine;
    private final List<UIElement> uiElements;

    private Button menuButton;
    private TextArea nameArea;

    public CustomizationTab(Engine parentEngine){
        this.uiElements = new ArrayList<>();
        this.parentEngine = parentEngine;
    }

    private void saveCustomization(){
        File serverFile = new File("save/customization.txt");
        try {
            PrintWriter pr = new PrintWriter(serverFile);
            pr.println(nameArea.getText());
            pr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        int centerX = parentEngine.width/2;
        int downLeftX = 50;
        int downLeftY = parentEngine.height-50;

        nameArea = new TextArea(false, 2, 12, centerX, 100, 200, 50,
                parentEngine, parentEngine.gameClient.loadName(), "Type a username...", "Username");

        menuButton = new Button(null, Assets.backButton, downLeftX, downLeftY, 50, 50, parentEngine);
        uiElements.add(menuButton);
        uiElements.add(nameArea);
    }

    public void tick() {
        for(var uiElement : uiElements)
            uiElement.tick();

        if(menuButton.isClicked()){
            saveCustomization();
            parentEngine.setCurrentScene(new MainMenu(parentEngine));
        }
    }

    public void render(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0,0, parentEngine.width, parentEngine.height);
        for(var uiElement : uiElements)
            uiElement.render(g);
    }
}
