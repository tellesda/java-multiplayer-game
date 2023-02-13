package anim;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Assets {

    //Tiles
    public static BufferedImage[] mapBlocks;

    //Animations
    public static BufferedImage[] joeFrontANIM, joeBackANIM, joeIdleANIM, joeBackIdleANIM,
            tonyFrontANIM, tonyBackANIM, tonyIdleANIM, tonyBackIdleANIM;

    //UI
    public static BufferedImage button, settingsButton, backButton, deleteButton, folderButton, textButton, imgButton,
                                exeButton, textBar;

    //Scenes
    public static BufferedImage computerScene;

    //Server messages

    //Other
    public static BufferedImage pointLight;

    public static int[] tilesWithCollision = {1,2,3,4,5,6,7,14,15,16,17,18,19,20,21,26,27,42,43,44,56,57,58,59,60,70,71,72,73,74,102,103,104};

    public static Font customFont;


    public static void init(){

        initBlocks(16,16,14,8);
        initUI();
        initJoe();
        initTony();
        initScenes();
        //initFont(24);
        pointLight = ImageReader.readImage("/res/textures/pointLight.png");

    }

    private static void initBlocks(int sWidth, int sHeight, int sheetX, int sheetY){
        SpriteSheet mapSheet = new SpriteSheet(ImageReader.readImage("/res/textures/mapAssets.png"));
        mapBlocks = new BufferedImage[sheetX*sheetY];
        int blockIndex = 0;
        for(int y=0; y<sheetY; y++){
            for(int x=0; x<sheetX; x++){
                mapBlocks[blockIndex] = mapSheet.crop(sWidth*x,sHeight*y,sWidth,sHeight);
                blockIndex++;
            }
        }
    }

    private static void initUI(){
        button = ImageReader.readImage("/res/textures/ui/button.png");
        backButton = ImageReader.readImage("/res/textures/ui/backButton.png");
        settingsButton = ImageReader.readImage("/res/textures/ui/settingsButton.png");
        deleteButton = ImageReader.readImage("/res/textures/ui/deleteButton.png");
        folderButton = ImageReader.readImage("/res/textures/ui/folder.png");
        textButton = ImageReader.readImage("/res/textures/ui/textFile.png");
        imgButton = ImageReader.readImage("/res/textures/ui/imgFile.png");
        exeButton = ImageReader.readImage("/res/textures/ui/exeFile.png");
        textBar = ImageReader.readImage("/res/textures/ui/textBar.png");
    }

    private static void initJoe(){
        SpriteSheet joeSheet = new SpriteSheet(ImageReader.readImage("/res/sprites/joe.png"));
        joeFrontANIM = new BufferedImage[4];
        joeFrontANIM[0] = joeSheet.crop(16*0, 0, 16, 32);
        joeFrontANIM[1] = joeSheet.crop(16*2, 0, 16, 32);
        joeFrontANIM[2] = joeSheet.crop(16*0, 0, 16, 32);
        joeFrontANIM[3] = joeSheet.crop(16*1, 0, 16, 32);

        joeBackANIM = new BufferedImage[4];
        joeBackANIM[0] = joeSheet.crop(16*0, 32, 16, 32);
        joeBackANIM[1] = joeSheet.crop(16*2, 32, 16, 32);
        joeBackANIM[2] = joeSheet.crop(16*0, 32, 16, 32);
        joeBackANIM[3] = joeSheet.crop(16*1, 32, 16, 32);

        joeIdleANIM = new BufferedImage[1];
        joeIdleANIM[0] = joeSheet.crop(0, 0, 16, 32);

        joeBackIdleANIM = new BufferedImage[1];
        joeBackIdleANIM[0] = joeSheet.crop(0, 32, 16, 32);

    }

    private static void initTony(){
        SpriteSheet tonySheet = new SpriteSheet(ImageReader.readImage("/res/sprites/tony.png"));
        tonyFrontANIM = new BufferedImage[4];
        tonyFrontANIM[0] = tonySheet.crop(16*0, 0, 16, 32);
        tonyFrontANIM[1] = tonySheet.crop(16*2, 0, 16, 32);
        tonyFrontANIM[2] = tonySheet.crop(16*0, 0, 16, 32);
        tonyFrontANIM[3] = tonySheet.crop(16*1, 0, 16, 32);

        tonyBackANIM = new BufferedImage[4];
        tonyBackANIM[0] = tonySheet.crop(16*0, 32, 16, 32);
        tonyBackANIM[1] = tonySheet.crop(16*2, 32, 16, 32);
        tonyBackANIM[2] = tonySheet.crop(16*0, 32, 16, 32);
        tonyBackANIM[3] = tonySheet.crop(16*1, 32, 16, 32);

        tonyIdleANIM = new BufferedImage[1];
        tonyIdleANIM[0] = tonySheet.crop(0, 0, 16, 32);

        tonyBackIdleANIM = new BufferedImage[1];
        tonyBackIdleANIM[0] = tonySheet.crop(0, 32, 16, 32);

    }

    private static void initScenes(){
        computerScene = ImageReader.readImage("/res/textures/computer.png");
    }

    public static void initFont(float size){
        try {
            //create the font to use. Specify the size!
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/VT323-Regular.ttf")).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(font);
            customFont = font;

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    //Debugging
    public static int sizeof(Object obj) throws IOException {

        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);

        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
        objectOutputStream.close();

        return byteOutputStream.toByteArray().length;
    }

}
