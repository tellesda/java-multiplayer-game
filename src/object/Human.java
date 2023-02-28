package object;

import anim.Animation;
import anim.Assets;
import debug.Timer;
import math.Vector2D;
import rendering.ShadowMap;
import rendering.TextureModifier;
import scene.World;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Human extends Entity implements Sortable{

    //Attributes
    protected World world;
    protected Animation animIDLE, animFront, animBack, animBackIDLE;
    protected Animation currentAnimation;
    protected  BufferedImage textureLit;
    protected byte animationIndex;
    protected final float moveSpeed;
    protected String lookDirection = "DOWN";
    protected final Timer timer;

    public Human(Vector2D location, World world, String type){
        super(location, new Vector2D(1f, 2f), new CollisionBounds(-0.5f,0.5f,-1f,1f, location));
        this.moveSpeed = 0.5f;
        if(type.equals("Joe")){
            this.animIDLE = new Animation(Assets.joeIdleANIM, 150);
            this.animFront = new Animation(Assets.joeFrontANIM, 150);
            this.animBack = new Animation(Assets.joeBackANIM, 150);
            this.animBackIDLE = new Animation(Assets.joeBackIdleANIM, 150);
        }
        else if(type.equals("Tony")){
            this.animIDLE = new Animation(Assets.tonyIdleANIM, 150);
            this.animFront = new Animation(Assets.tonyFrontANIM, 150);
            this.animBack = new Animation(Assets.tonyBackANIM, 150);
            this.animBackIDLE = new Animation(Assets.tonyBackIdleANIM, 150);
        }
        this.currentAnimation = animIDLE;
        this.world = world;
        this.timer = new Timer(0.01f, world);
        this.textureLit = new BufferedImage(
                currentAnimation.getCurrentFrame().getWidth(),
                currentAnimation.getCurrentFrame().getHeight(),
                currentAnimation.getCurrentFrame().getType());

    }

    public World getWorld() {
        return world;
    }

    public Vector2D getFeetLocation(){
        return new Vector2D(location.getX(), location.getY()+1f);
    }

    public void setFeetLocation(Vector2D location){
        setLocation(new Vector2D(location.getX(), location.getY()-1f));
    }

    public float getY() {
        return this.location.getY()+1;
    }

    public byte getAnimationIndex() {
        return animationIndex;
    }

    public void setAnimationIndex(byte animationIndex) {
        this.animationIndex = animationIndex;
    }

    private void setCurrentAnimation(Animation animation) {
        if(currentAnimation != animation){
            animation.setIndex(0);
            this.currentAnimation = animation;
        }
    }

    protected void setForwardANIM(){
        animationIndex = 1;
        lookDirection = "DOWN";
    }
    protected  void setBackANIM(){
        animationIndex = 2;
        lookDirection = "UP";
    }
    protected void setIdleANIM(){
       switch (lookDirection){
           case "DOWN" -> animationIndex = 0;
           case "UP" -> animationIndex = 3;
       }
    }

    protected void updateAnimation(){
        switch (animationIndex){
            case 0 -> setCurrentAnimation(animIDLE);
            case 1 -> setCurrentAnimation(animFront);
            case 2 -> setCurrentAnimation(animBack);
            case 3 -> setCurrentAnimation(animBackIDLE);
        }
    }

    public void updateTextureLit(ShadowMap shadowMap){
        BufferedImage frame = this.currentAnimation.getCurrentFrame();
        TextureModifier.applyShadowMap(frame,this.textureLit, shadowMap, this);
    }

    public void init(){}

    public void tick(){
        super.tick();
        timer.tick();
        updateAnimation();
        currentAnimation.tick();
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

        if(timer.ticking)
            updateTextureLit(world.getLevel().getShadowMap());

        g.drawImage(textureLit, (int)sized.getX(), (int)sized.getY(), resultWidth, resultHeight, null);
    }

}
