package anim;

import java.awt.image.BufferedImage;

public class Animation {

    private final int speed;
    private int index;
    private long lastTime,timer;
    private final BufferedImage[] frames;

    public Animation(BufferedImage[] frames, int speed){
        this.speed = speed;
        this.frames = frames;
        index = 0;
        timer = 0;
        lastTime = System.currentTimeMillis();
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void tick(){

        timer += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();

        if(timer > speed){
            index++;
            timer = 0;
            if(index >= frames.length){
                index = 0;
            }
        }
    }

    public BufferedImage getCurrentFrame() {
        return frames[index];
    }
}
