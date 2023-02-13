package object.interactive.computer;

import anim.ImageReader;
import engine.Engine;
import input.KeyManager;
import object.interactive.computer.snake.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class SnakeFile extends File{

    private BufferedImage gameScreen;
    private BufferedImage gameWon;
    private BufferedImage gameOver;
    private Graphics2D graphics2D;
    private final int gridSize = 16, speed = 150;
    private long lastTime, timer;
    private final KeyManager keyManager;
    private boolean isGameOver = false;
    private boolean isGameWon = false;

    private Body head;
    private Fruit fruit;

    private int score;
    private byte headDirection; //0=none, 1=up, 2=down, 3=left, 4=right

    public SnakeFile(String name, Folder parent, Computer parentComputer, Engine parentEngine){
        super(name, parent, parentComputer);
        this.timer = 0;
        this.lastTime = System.currentTimeMillis();
        this.keyManager = parentEngine.getKeyManager();
    }

    public void loadContent(){
        this.gameScreen = new BufferedImage(gridSize, gridSize, BufferedImage.TYPE_INT_RGB);
        graphics2D = gameScreen.createGraphics();
        this.gameWon = ImageReader.readImage("/res/textures/ui/gameWon.png");
        this.gameOver = ImageReader.readImage("/res/textures/ui/gameOver.png");

        this.score = 0;
        this.headDirection = 0;
        this.head = new Body(null, null);
        this.head.setX(gridSize/2);
        this.head.setY(gridSize/2);
        this.fruit = new Fruit(gridSize/4,gridSize/4);
        this.isGameOver = false;
        this.isGameWon = false;
    }

    public void clearContent(){
        this.gameScreen = null;
        this.graphics2D = null;
        this.gameWon = null;
        this.gameOver = null;
    }

    public void updateGame(){

        //Game state logic
        if(head.getX() < 0 | head.getX() >= gridSize | head.getY() < 0 | head.getY() >= gridSize)
            isGameOver = true;

        if(head.getChild() != null)
            if(Body.collidesWithSnake(head.getX(), head.getY(), head.getChild()))
                isGameOver = true;

        if(score >= gridSize*gridSize - 1){
            isGameWon = true;
        }

        //Game logic
        if(headDirection == 1){
            head.setLocation(head.getX(), head.getY()-1);
        }
        if(headDirection == 2){
            head.setLocation(head.getX(), head.getY()+1);
        }
        if(headDirection == 3){
            head.setLocation(head.getX()-1, head.getY());
        }
        if(headDirection == 4){
            head.setLocation(head.getX()+1, head.getY());
        }

        if(head.getX() == fruit.getX() && head.getY() == fruit.getY()){
            Body.addTail(head);
            Body.moveTail(head);
            fruit.move(head, gridSize);
            score++;
        }
        else{
            Body.moveTail(head);
        }

        //Update render
        graphics2D.setColor(new Color(16,16,16));
        graphics2D.fillRect(0,0,gridSize,gridSize);
        graphics2D.setColor(parentComputer.getTextColor());
        Body.drawSnake(graphics2D, head);
        graphics2D.setColor(Color.YELLOW);
        graphics2D.fillRect(fruit.getX(), fruit.getY(), 1,1); //Draw fruit
    }

    public void tick() {

        if(isGameOver | isGameWon){
            if(keyManager.keyW | keyManager.keyS | keyManager.keyA | keyManager.keyD)
                loadContent();
            return;
        }

        timer += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();

        if(timer > speed){
            updateGame();
            timer = 0;
        }

        //Player input
        if(keyManager.keyW)
            headDirection = 1;
        if(keyManager.keyS)
            headDirection = 2;
        if(keyManager.keyA)
            headDirection = 3;
        if(keyManager.keyD)
            headDirection = 4;
    }

    public void render(Graphics g){
        g.setColor(parentComputer.getTextColor());
        //Draw game padding
        int scale = (int)(getButton().scaleX*1.04);
        g.fillRect(parentComputer.getRoot().getButton().posX-(int)(getButton().scaleX*0.338),
                parentComputer.getRoot().getButton().posY-(int)(getButton().scaleX*0.17),
                scale,
                scale);

        //Draw game
        BufferedImage imageToDraw = gameScreen;

        if(isGameOver)
            imageToDraw = gameOver;
        else if(isGameWon)
            imageToDraw = gameWon;

        g.drawImage(imageToDraw, parentComputer.getRoot().getButton().posX-(int)(getButton().scaleX*0.32),
                parentComputer.getRoot().getButton().posY-(int)(getButton().scaleX*0.15),
                getButton().scaleX,
                getButton().scaleX, null);
    }

}
