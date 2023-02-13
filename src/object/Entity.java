package object;

import object.ai.pathFinding.Interpolator;
import math.Vector2D;
import scene.World;

import java.awt.Graphics;
import java.util.LinkedList;

public abstract class Entity {

    //Attributes
    protected Vector2D location;
    protected Vector2D scale;
    protected CollisionBounds collision;
    protected final LinkedList<Interpolator> interpolators;

    public Entity(Vector2D location, Vector2D scale, CollisionBounds collision){
        this.collision = collision;
        this.location = location;
        this.scale = scale;
        this.interpolators = new LinkedList<>();
    }

    public LinkedList<Interpolator> getInterpolators() {
        return interpolators;
    }

    public void setCollision(CollisionBounds collision) {
        this.collision = collision;
    }

    public Vector2D getLocation() {
        return location;
    }
    public Vector2D getScale() {
        return scale;
    }
    public CollisionBounds getCollision() {
        return collision;
    }

    public void setLocation(Vector2D location) {
        if(collision != null)
            collision.updateWorldCollision(location);
        this.location = location;
    }

    public void interpolateLocation(Vector2D start, Vector2D end, int speed){
        this.interpolators.add(new Interpolator(start,end,speed));
    }

    public abstract void init();

    public void tick(){
        if(interpolators.size() == 0)
            return;

        Interpolator currentInterpolator = interpolators.peek();

        currentInterpolator.tick();

        if(!currentInterpolator.isFinished())
            setLocation(currentInterpolator.getCurrent()); //getCurrent location
        else
            interpolators.remove();
    }

    public abstract void render(Graphics g, int width, int height, World world);}
