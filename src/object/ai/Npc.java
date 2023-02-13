package object.ai;

import object.ai.pathFinding.Interpolator;
import math.Vector2D;
import object.Human;
import object.ai.pathFinding.Node;
import scene.World;

import java.util.List;

public abstract class Npc extends Human {

    public Npc(Vector2D location, World world) {
        super(location, world, "Tony");
    }


    public void moveTo(Vector2D goal, int moveSpeed){

        int adjustedSpeed = moveSpeed + world.getParentEngine().engineTicks/2; //Adds a constant

        if(interpolators.size() != 0)
            return;

        Vector2D start = world.getLevel().getNearestBlock(getFeetLocation(), 2, true).getLocation();
        List<Node> navPoints = world.getLevel().getNavGrid().getPath(start, goal);

        Vector2D lastNodeLocation = null;
        boolean firstNode = true;

        for(var node : navPoints){
            Vector2D startLocation;
            if(firstNode){
                startLocation = getFeetLocation();
                firstNode = false;
            }
            else
                startLocation = lastNodeLocation;

            interpolateLocation(startLocation, node.getLocation(), adjustedSpeed);

            lastNodeLocation = node.getLocation();
        }
    }

    private boolean isWalkingUp(Vector2D start, Vector2D end){
        return start.getY() > end.getY();
    }

    @Override
    public void tick(){
        updateAnimation();
        currentAnimation.tick();

        if(interpolators.size() == 0){
            setIdleANIM();
            return;
        }

        Interpolator currentInterpolator = interpolators.peek();

        currentInterpolator.tick();

        if(currentInterpolator.getDirection().equals("UP"))
            setBackANIM();
        else
            setForwardANIM();

        if(!currentInterpolator.isFinished())
            setFeetLocation(currentInterpolator.getCurrent()); //getCurrent location
        else
            interpolators.remove();
    }
}
