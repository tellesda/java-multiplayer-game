package object.interactive.computer;

import anim.Assets;
import engine.Engine;
import math.Vector2D;
import object.*;
import object.interactive.Interactive;
import rendering.LitObject;
import rendering.ShadowMap;
import rendering.TextureModifier;
import scene.ComputerScene;
import java.awt.Color;
import scene.World;
import ui.Button;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Computer extends Entity implements Interactive, LitObject, Sortable {

    private final Engine parentEngine;
    private final String name;
    private final Color textColor;
    private final BufferedImage computerTexture;
    private BufferedImage textureLit;
    private Folder root;
    private File currentFile;

    public Computer(String name, Vector2D location, Color textColor, Engine parentEngine){
        super(location, new Vector2D(1f, 1f), new CollisionBounds(-0.5f,0.5f,-0.5f,0.5f, location));
        this.computerTexture = Assets.mapBlocks[110];
        this.name = name;
        this.textColor = textColor;
        this.parentEngine = parentEngine;
    }

    public float getY() {
        return this.location.getY()+0.5f;
    }

    public Folder getRoot() {
        return root;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile.clearContent();
        this.currentFile = currentFile;
        currentFile.loadContent();
    }

    public String getName() {
        return name;
    }

    public void updateLightTexture(ShadowMap shadowMap){
        this.textureLit = TextureModifier.applyShadowMap(computerTexture, shadowMap, this);
    }

    public void load(ComputerScene computerScene){

        //Setup root
        int windowPosX = computerScene.getWindowPosX();
        int windowPosY = computerScene.getWindowPosY();
        int windowWidth = computerScene.getWindowWidth();

        root = new Folder("root", null, this);
        root.setButton(new Button(root.getName(), null,
                windowPosX+(int)(windowWidth*0.42), windowPosY+(windowWidth/4),
                (int)(windowWidth*0.4), windowWidth/20, parentEngine));
        root.getButton().setAlignLeft(true);

        this.currentFile = root;
        String path = "res/computers/" + name;
        try {
            BufferedReader bf = new BufferedReader(new FileReader(path));

            String currentLine;
            while((currentLine = bf.readLine()) != null){
                String[] parts = currentLine.split(" ");
                String type = parts[0];

                if(type.equals("f")){
                    insertFolder(parts);
                    continue;
                }
                if(type.equals("t")){
                    insertTextFile(parts);
                    continue;
                }
                if(type.equals("s")){
                    insertSnake(parts);
                    continue;
                }
                if(type.equals("i")){
                    insertImageFile(parts);
                    continue;
                }
                if(type.equals("w")){
                    insert3dWorld(parts);
                }
            }

            bf.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear(){
        root = null;
    }

    private void insertFolder(String[] filePath){
        Folder current = root;

        //Inserting folder
        for(int i=1; i< filePath.length; i++){
            Folder nextFolder = (Folder) current.getFileByName(filePath[i]);
            //Case where the folder doesn't exist
            if(nextFolder == null){
                Folder newFolder = new Folder(filePath[i], current, this);
                current.addToFiles(newFolder, Assets.folderButton);
                current = newFolder;
            }
            //Case where the folder is found
            else{
                current = nextFolder;
            }
        }
    }

    private void insertTextFile(String[] filePath){

        //Inserting folder
        Folder current = root;
        for(int i=1; i< filePath.length-1; i++){
            Folder nextFolder = (Folder) current.getFileByName(filePath[i]);
            //Case where the folder doesn't exist
            if(nextFolder == null){
                Folder newFolder = new Folder(filePath[i], current, this);
                current.addToFiles(newFolder, Assets.folderButton);
                current = newFolder;
            }
            //Case where the folder is found
            else{
                current = nextFolder;
            }
        }
        current.addToFiles(new TextFile(filePath[filePath.length-1], current, this), Assets.textButton);
    }

    private void insertImageFile(String[] filePath){

        //Inserting folder
        Folder current = root;
        for(int i=1; i< filePath.length-1; i++){
            Folder nextFolder = (Folder) current.getFileByName(filePath[i]);
            //Case where the folder doesn't exist
            if(nextFolder == null){
                Folder newFolder = new Folder(filePath[i], current, this);
                current.addToFiles(newFolder, Assets.folderButton);
                current = newFolder;
            }
            //Case where the folder is found
            else{
                current = nextFolder;
            }
        }
        current.addToFiles(new ImageFile(filePath[filePath.length-1], current, this), Assets.imgButton);
    }

    private void insertSnake(String[] filePath){

        //Inserting folder
        Folder current = root;
        for(int i=1; i< filePath.length-1; i++){
            Folder nextFolder = (Folder) current.getFileByName(filePath[i]);
            //Case where the folder doesn't exist
            if(nextFolder == null){
                Folder newFolder = new Folder(filePath[i], current, this);
                current.addToFiles(newFolder, Assets.folderButton);
                current = newFolder;
            }
            //Case where the folder is found
            else{
                current = nextFolder;
            }
        }
        current.addToFiles(new SnakeFile(filePath[filePath.length-1], current, this, parentEngine), Assets.exeButton);
    }

    private void insert3dWorld(String[] filePath){

        //Inserting folder
        Folder current = root;
        for(int i=1; i< filePath.length-1; i++){
            Folder nextFolder = (Folder) current.getFileByName(filePath[i]);
            //Case where the folder doesn't exist
            if(nextFolder == null){
                Folder newFolder = new Folder(filePath[i], current, this);
                current.addToFiles(newFolder, Assets.folderButton);
                current = newFolder;
            }
            //Case where the folder is found
            else{
                current = nextFolder;
            }
        }
        current.addToFiles(new ThreeDimensionFile(filePath[filePath.length-1], current, this, parentEngine), Assets.exeButton);
    }

    public void interact(Player player){
        player.setUiScene(new ComputerScene(player, this));
    }

    public void renderScreen(Graphics g){
        currentFile.render(g);
    }

    public void init(){}

    public void tick(){
        super.tick();
        currentFile.tick();
    }

    public void render(Graphics g, int width, int height, World world){

        Camera camera = world.getCamera();

        float cameraScaling = 1/camera.getScaling();
        Vector2D worldSpace = Vector2D.sub(location, new Vector2D(scale.getX()*0.5f, scale.getY()*0.5f));
        Vector2D camSpace = Vector2D.add(camera.getInversePosition(), worldSpace);
        Vector2D sized = Vector2D.add(Vector2D.mul(camSpace, new Vector2D(64*cameraScaling, 64*cameraScaling)),
                new Vector2D(width*0.5f, height*0.5f));

        int resultWidth = (int)(64*scale.getX()*cameraScaling);
        int resultHeight = (int)(64*scale.getY()*cameraScaling);

        g.drawImage(this.textureLit, (int)sized.getX(), (int)sized.getY(), resultWidth, resultHeight, null);
    }
}
