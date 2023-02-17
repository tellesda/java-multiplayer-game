package object.furnitures;

import com.esotericsoftware.jsonbeans.JsonReader;
import com.esotericsoftware.jsonbeans.JsonValue;
import debug.DebugPoint;
import math.Vector2D;
import object.Block;
import object.CollisionBounds;
import object.Entity;
import object.Sortable;
import rendering.LitObject;
import rendering.ShadowMap;
import scene.World;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Furniture extends Entity implements LitObject, Sortable {

    enum Direction {
        FRONT,
        BACK,
        LEFT,
        RIGHT
    }

    private final int idx;
    private String name;
    private float y_offset;
    private Direction direction;

    private List<Block> blocksFRONT;
    private List<Block> blocksBACK;
    private List<Block> blocksLEFT;
    private List<Block> blocksRIGHT;

    public Furniture(int idx, Vector2D location, String dir){
        super(location, new Vector2D(1,1), new CollisionBounds(-1, 1, -1, 0.5f, location));
        this.idx = idx;
        this.direction = Direction.valueOf(dir);
        loadFurniture();

    }

    public float getY() {
        return this.location.getY()+y_offset;
    }

    public List<Block> getBlocks() {

        List<Block> blocks;

        switch (direction){
            case BACK -> blocks = blocksBACK;
            case LEFT -> blocks = blocksLEFT;
            case RIGHT -> blocks = blocksRIGHT;
            default -> blocks = blocksFRONT;
        }

        return blocks;
    }

    public void loadFurniture(){

        this.blocksFRONT = new ArrayList<>(4);
        this.blocksLEFT = new ArrayList<>(4);
        this.blocksRIGHT = new ArrayList<>(4);
        this.blocksBACK = new ArrayList<>(4);

        try {
            FileReader file = new FileReader("res/furniture/furniture.json");
            JsonReader jsonReader = new JsonReader();

            JsonValue jsonValue = jsonReader.parse(file);
            jsonValue = jsonValue.get("furniture").get(idx);

            this.name = jsonValue.get("name").toString();
            this.y_offset = jsonValue.get("y_offset").asFloat();


            for(Direction dir : Direction.values()){

                String blocksName = "blocks"+dir;
                int size = jsonValue.get(blocksName).size();

                for(int i=0; i<size; i++){
                    JsonValue jv = jsonValue.get(blocksName).get(i);
                    int blockIdx = jv.get("idx").asInt();

                    jv = jv.get("location");
                    Vector2D blockLocation = new Vector2D(jv.get(0).asFloat(), jv.get(1).asFloat());
                    Vector2D wsBlockLocation = Vector2D.add(location, blockLocation);

                    switch (dir){
                        case BACK -> blocksBACK.add(new Block(wsBlockLocation, blockIdx));
                        case LEFT -> blocksLEFT.add(new Block(wsBlockLocation, blockIdx));
                        case RIGHT -> blocksRIGHT.add(new Block(wsBlockLocation, blockIdx));
                        case FRONT -> blocksFRONT.add(new Block(wsBlockLocation, blockIdx));
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics g, int width, int height, World world) {
        for(var block : getBlocks()){
            block.render(g, width, height, world);
        }
    }


    public void updateLightTexture(ShadowMap shadowMap) {
        for(var block : getBlocks())
            block.updateLightTexture(shadowMap);
    }


    public Vector2D getLocation() {
        return location;
    }

    public void init() {

    }
}
