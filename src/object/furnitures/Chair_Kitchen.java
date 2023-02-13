package object.furnitures;
import math.Vector2D;
import object.Block;
import java.util.ArrayList;

public class Chair_Kitchen extends Furniture{

    public Chair_Kitchen(Vector2D location){

        super(location);

        this.blocksFront = new ArrayList<>(2);
        blocksFront.add(new Block(new Vector2D(location.getX(), location.getY()-0.5f), 88));
        blocksFront.add(new Block(new Vector2D(location.getX(), location.getY()+0.5f), 102));

        this.blocksBack = new ArrayList<>(2);
        blocksBack.add(new Block(new Vector2D(location.getX(), location.getY()-0.5f), 88));
        blocksBack.add(new Block(new Vector2D(location.getX(), location.getY()+0.5f), 102));

        this.blocksLeft = new ArrayList<>(2);
        blocksLeft.add(new Block(new Vector2D(location.getX(), location.getY()-0.5f), 89));
        blocksLeft.add(new Block(new Vector2D(location.getX(), location.getY()+0.5f), 103));

        this.blocksRight = new ArrayList<>(2);
        blocksRight.add(new Block(new Vector2D(location.getX(), location.getY()-0.5f), 87));
        blocksRight.add(new Block(new Vector2D(location.getX(), location.getY()+0.5f), 101));
    }

    public float getY() {
        return this.location.getY()+0.5f;
    }
}
