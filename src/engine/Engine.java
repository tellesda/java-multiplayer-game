package engine;

import anim.Assets;
import input.KeyManager;
import input.MouseManager;
import network.GameClient;
import network.GameServer;
import network.ServerInfo;
import scene.*;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Engine implements Runnable {

    //Attributes
    public Engine hostedServer;
    public ServerInfo serverInfo;

    public boolean isServer;
    public GameServer gameServer;
    public GameClient gameClient;

    private Display display;
    public int width, height;
    public int fps;
    public int engineTicks;

    private Scene currentScene;

    private boolean running = false;
    private Thread thread;

    private KeyManager keyManager;
    private MouseManager mouseManager;

    //Constructor
    public Engine(int width, int height, int fps, ServerInfo serverInfo){
        if(serverInfo != null)
            this.isServer = true;
        this.serverInfo = serverInfo;
        this.width = width;
        this.height = height;
        this.fps = fps;
        this.engineTicks = fps;

        if(isServer){
            return;
        }

        this.gameClient = new GameClient(this);
        this.keyManager = new KeyManager();
        this.mouseManager = new MouseManager();
    }

    public KeyManager getKeyManager() { return keyManager; }
    public MouseManager getMouseManager() {return mouseManager;}
    public void setCurrentScene(Scene scene) {
        scene.init();
        this.currentScene = scene;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void setResolution(int width, int height){
        this.width = width;
        this.height = height;

        boolean fullScreen = (width == 1920 && height == 1080);

        display.createDisplay(width, height, fullScreen);
        display.getFrame().addKeyListener(keyManager);
        display.getFrame().addMouseListener(mouseManager);
        display.getFrame().addMouseMotionListener(mouseManager);
        display.getCanvas().addMouseListener(mouseManager);
        display.getCanvas().addMouseMotionListener(mouseManager);
        //commandLine.adjustResolution(height);
        setCurrentScene(new SettingsTab(this));

    }

    private void init(){
        Assets.init();
        if(!isServer){
            display = new Display(width, height);
            display.getFrame().addKeyListener(keyManager);
            display.getFrame().addMouseListener(mouseManager);
            display.getFrame().addMouseMotionListener(mouseManager);
            display.getCanvas().addMouseListener(mouseManager);
            display.getCanvas().addMouseMotionListener(mouseManager);
            setResolution(width, height);
            setCurrentScene(new MainMenu(this));
        }
        else{
            World world = new World(this);
            this.gameServer = new GameServer(world, serverInfo);
            setCurrentScene(world);
        }
    }

    int x = 0;

    private void tick(){
        x += 1;

        if(!isServer)
            keyManager.tick();

        currentScene.tick();
    }

    private void render(){
        if(isServer)
            return;

        BufferStrategy bs = display.getCanvas().getBufferStrategy();
        if(bs == null){
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setFont(Assets.customFont);
        //Clear Screen
        g.clearRect(0, 0, width, height);
        //TODO RENDER HERE
        currentScene.render(g);

        //Ends render
        bs.show();
        g.dispose();
    }

    public void run(){

        init();

        int fps = this.fps;
        double timePerTick = 1000000000f / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;
        long timer2 = 0;
        int ticks = 0;
        int ticks2 = 0;

        while(running){
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            timer2 += now - lastTime;
            lastTime = now;

            if(delta >= 1){
                tick();
                render();
                ticks++;
                ticks2++;
                delta--;
            }

            if(timer2 >= 100000000){
                this.engineTicks = ticks2;
                ticks2 = 0;
                timer2 = 0;
            }

            if(timer >= 1000000000){
                if(!isServer){
                    fps = this.fps;
                    timePerTick = 1000000000f / fps;
                    display.getFrame().setTitle("fps : " + ticks);
                }
                ticks = 0;
                timer = 0;
            }
        }

        stop();

    }

    public void start(){
        if(running)
            return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stop(){
        if(!running)
            return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Utility functions

    public float adjustedSpeed(float speed){
        float scalar = 1 / (float)this.engineTicks;
        return scalar * speed;
    }

    public static short floatToShort(float value){
        return (short)(value*100);
    }
    public static float shortToFloat(short value){
        return ((float)value)/100;
    }

}











