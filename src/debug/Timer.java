package debug;

import scene.World;

public class Timer {

    private final World parentWorld;
    public final float seconds;
    public int nFrames;
    public int n;
    public boolean ticking;

    public Timer(float seconds, World world){
        this.parentWorld = world;
        this.seconds = seconds;
    }

    public void tick(){

        ticking = false;

        this.nFrames = (int)(parentWorld.getParentEngine().engineTicks * 10 * seconds);

        n++;

        if(n >= nFrames){
            n = 0;
            ticking = true;
        }
    }
}
