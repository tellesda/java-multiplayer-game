package object;

import input.KeyManager;
import input.MouseManager;
import math.Vector2D;
import object.interactive.Interactive;
import scene.MainMenu;
import scene.Scene;
import scene.World;
import java.util.ArrayList;
import java.util.List;


public class Player extends Human{

    //Interaction
    private boolean isPressingE;
    private boolean isClicking;
    private boolean isInteracting;
    //Movement
    private boolean isMovingY;
    private boolean isMovingX;
    //Other
    private final KeyManager keyManager;
    private final MouseManager mouseManager;

    //UI
    private Scene uiScene; //Interaction screen

    public Player(Vector2D location, World world){
        super(location, world, "Joe");
        this.keyManager = world.getParentEngine().getKeyManager();
        this.mouseManager = world.getParentEngine().getMouseManager();
    }

    public Scene getUiScene(){
        return this.uiScene;
    }

    public void setUiScene(Scene uiScene) {
        this.uiScene = uiScene;
    }

    public void setInteracting(boolean interacting) {
        isInteracting = interacting;
    }

    @Override
    public void setLocation(Vector2D location) {

        collision.updateWorldCollision(location);

        List<Block> blocks = new ArrayList<>(world.getLevel().getNearBlocks(getFeetLocation(), 2));

        for(var door : world.getLevel().getDoors())
            if(Vector2D.fastDistance(door.getLocation(), getFeetLocation()) < 2)
                blocks.add(door.getDoorBottom());

        for(var fur : world.getLevel().getFurnitures()) //TODO fix
            if(Vector2D.fastDistance(fur.getLocation(), getFeetLocation()) < 3)
                blocks.addAll(fur.getBlocks());


        boolean isLocationValid = true;
        for(var block : blocks){
            try {
                if(!block.hasCollision())
                    continue;
            }
            catch (NullPointerException e){
                this.location = location;
                return;
            }

            Vector2D leftFoot = new Vector2D(location.getX()-0.3f, location.getY()+1);
            Vector2D rightFoot = new Vector2D(location.getX()+0.3f, location.getY()+1);
            if(block.collision.isLineInside(leftFoot, rightFoot)){
                isLocationValid = false;
                break;
            }
        }
        if(isLocationValid)
            this.location = location;
        else
            collision.updateWorldCollision(this.location);
    }

    public void moveY(float amount){
        if(amount > 0){
            setForwardANIM();
        }
        else{
            setBackANIM();
        }

        float adjustedSpeed = world.getParentEngine().adjustedSpeed(amount);
        if(isMovingX)
            adjustedSpeed = adjustedSpeed * 0.7071f;
        setLocation(new Vector2D(location.getX(), location.getY()+adjustedSpeed));
        isMovingY = true;
    }

    public void moveX(float amount){
        if(animationIndex == 0)
            setForwardANIM();
        else if(animationIndex == 3)
            setBackANIM();
        float adjustedSpeed = world.getParentEngine().adjustedSpeed(amount);
        if(isMovingY)
            adjustedSpeed = adjustedSpeed * 0.7071f;
        setLocation(new Vector2D(location.getX()+adjustedSpeed, location.getY()));
        this.isMovingX = true;
    }

    private void ePress(){
        if(keyManager.keyE){
            if(!isPressingE){
                //Runs once here
                if(!isInteracting)
                    interact(this.location, 2);
                else
                    stopInteraction();
                isPressingE = true;
            }
        }
        else
            isPressingE = false;
    }

    private void escPress(){
        if(keyManager.keyESC){
            world.getParentEngine().setCurrentScene(new MainMenu(world.getParentEngine()));
            //Disconnect from server
            if(world.getParentEngine().gameClient.getClient().isConnected())
                world.getParentEngine().gameClient.disconnect();

            //Close server if hosting
            if(world.getParentEngine().hostedServer != null){
                world.getParentEngine().hostedServer.gameServer.stopServer();
                world.getParentEngine().hostedServer = null;
            }
        }
    }

    private void leftMouseClick(){
        if(mouseManager.isLeftPressed()){
            if(!isClicking){
                isClicking = true;
                //Runs once here
                Vector2D clickLocation = mouseManager.getClickWorldLocation(world);
                if(!isInteracting){
                    if(Vector2D.distance(this.location, clickLocation) < 2)
                        interact(clickLocation, 1);
                }
                Block nearest = world.getLevel().getNearestBlock(clickLocation, 3, true);
                if(nearest != null){
                    //...
                }
            }

        }
        else
            isClicking = false;
    }

    //Interact with the nearest object in range
    private void interact(Vector2D interactLocation, float interactDistance){
        List<Interactive> interactiveObjects = world.getLevel().getInteractiveObjects();

        Interactive nearest = null;
        float nearestDistance = 10000;
        for(var object : interactiveObjects){
            float distance = Vector2D.distance(object.getLocation(), interactLocation);

            if(distance < nearestDistance && distance < interactDistance){
                nearest = object;
                nearestDistance = distance;
            }
        }

        if(nearest != null){
            isInteracting = true;
            nearest.interact(this);
        }
    }

    public void stopInteraction(){
        this.isInteracting = false;
        this.uiScene = null;
    }

    public void tick(){
        super.tick();

        world.getParentEngine().gameClient.sendMovementInfo();

        setIdleANIM();

        if(!isInteracting){
            if(keyManager.keyW)
                moveY(-moveSpeed);
            if(keyManager.keyS)
                moveY(moveSpeed);
            if(keyManager.keyA)
                moveX(-moveSpeed);
            if(keyManager.keyD)
                moveX(moveSpeed);
        }

        if(!(keyManager.keyW || keyManager.keyS))
            isMovingY = false;
        if(!(keyManager.keyA || keyManager.keyD))
            isMovingX = false;

        escPress();
        ePress();
        leftMouseClick();


        world.getCamera().setLocation(location);

        if(uiScene != null)
            uiScene.tick();

    }

}
