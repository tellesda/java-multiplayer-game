package object.furnitures;
import math.Vector2D;
import object.Block;
import java.util.ArrayList;

public class Couch extends Furniture{

    public Couch(Vector2D location){

        super(location);

        this.blocksFront = new ArrayList<>(4);
        blocksFront.add(new Block(new Vector2D(location.getX()-0.5f, location.getY()-0.5f), 12));
        blocksFront.add(new Block(new Vector2D(location.getX()+0.5f, location.getY()-0.5f), 13));
        blocksFront.add(new Block(new Vector2D(location.getX()-0.5f, location.getY()+0.5f), 26));
        blocksFront.add(new Block(new Vector2D(location.getX()+0.5f, location.getY()+0.5f), 27));

        this.blocksBack = new ArrayList<>(4);
        blocksBack.add(new Block(new Vector2D(location.getX()-0.5f, location.getY()-0.5f), 12));
        blocksBack.add(new Block(new Vector2D(location.getX()+0.5f, location.getY()-0.5f), 13));
        blocksBack.add(new Block(new Vector2D(location.getX()-0.5f, location.getY()+0.5f), 26));
        blocksBack.add(new Block(new Vector2D(location.getX()+0.5f, location.getY()+0.5f), 27));

        this.blocksLeft = new ArrayList<>(4);
        blocksLeft.add(new Block(new Vector2D(location.getX()-0.5f, location.getY()-0.5f), 12));
        blocksLeft.add(new Block(new Vector2D(location.getX()+0.5f, location.getY()-0.5f), 13));
        blocksLeft.add(new Block(new Vector2D(location.getX()-0.5f, location.getY()+0.5f), 26));
        blocksLeft.add(new Block(new Vector2D(location.getX()+0.5f, location.getY()+0.5f), 27));

        this.blocksRight = new ArrayList<>(4);
        blocksRight.add(new Block(new Vector2D(location.getX()-0.5f, location.getY()-0.5f), 12));
        blocksRight.add(new Block(new Vector2D(location.getX()+0.5f, location.getY()-0.5f), 13));
        blocksRight.add(new Block(new Vector2D(location.getX()-0.5f, location.getY()+0.5f), 26));
        blocksRight.add(new Block(new Vector2D(location.getX()+0.5f, location.getY()+0.5f), 27));
    }

    public float getY() {
        return this.location.getY()+0.5f;
    }
}
