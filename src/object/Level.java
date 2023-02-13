package object;

import math.Vector2D;
import object.ai.pathFinding.NavGrid;
import object.furnitures.Chair_Kitchen;
import object.furnitures.Couch;
import object.furnitures.Furniture;
import object.interactive.Door;
import object.interactive.Interactive;
import object.interactive.computer.Computer;
import rendering.LitObject;
import rendering.ShadowMap;
import scene.World;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Level {

    public boolean isLoading;
    private final World parentWorld;
    private int currentLevel;
    private Block[][] tileGrid;
    private int mapSizeX, mapSizeY, spawnX, spawnY;
    private NavGrid navGrid;
    private List<PointLight> pointLights;
    private List<Door> doors;
    private List<Interactive> interactiveObjects;
    private List<LitObject> litObjects;
    private List<Furniture> furnitures;
    private ShadowMap shadowMap;

    public Level(World parentWorld){
        this.parentWorld = parentWorld;
    }

    public List<PointLight> getPointLights() {
        return pointLights;
    }
    public ShadowMap getShadowMap() {
        return shadowMap;
    }
    public List<Interactive> getInteractiveObjects() {
        return interactiveObjects;
    }
    public List<LitObject> getLitObjects() {
        return litObjects;
    }
    public List<Door> getDoors() {
        return doors;
    }
    public Block[][] getTileGrid() {
        return tileGrid;
    }
    public int getMapSizeX() {
        return mapSizeX;
    }
    public int getMapSizeY() {
        return mapSizeY;
    }
    public NavGrid getNavGrid() {
        return navGrid;
    }
    public List<Furniture> getFurnitures() {
        return furnitures;
    }
    public int getCurrentLevel() {
        return currentLevel;
    }
    public int getSpawnX() {
        return spawnX;
    }
    public int getSpawnY() {
        return spawnY;
    }

    public List<Block> getNearBlocks(Vector2D location, int searchRadius){
        int startX = getLeftTileIndex(location, searchRadius);
        int endX = getRightTileIndex(location, searchRadius);
        int startY = getUpTileIndex(location, searchRadius);
        int endY = getBottomTileIndex(location, searchRadius);

        List<Block> blocks = new ArrayList<>();
        for(int m = startY; m<endY; m++)
            for(int n = startX; n<endX; n++)
                blocks.add(getTileGrid()[m][n]);

        return blocks;
    }

    public Block getNearestBlock(Vector2D location, int searchRadius, boolean noCollisionOnly){

        if(isLoading)
            return null;

        int startX = getLeftTileIndex(location, searchRadius);
        int endX = getRightTileIndex(location, searchRadius);
        int startY = getUpTileIndex(location, searchRadius);
        int endY = getBottomTileIndex(location, searchRadius);

        Block nearest = null;
        float minDistance = 10000;
        for(int m = startY; m<endY; m++)
            for(int n = startX; n<endX; n++){
                Block current = getTileGrid()[m][n];
                if(current == null)
                    return null;
                if(noCollisionOnly && current.hasCollision())
                    continue;
                float distance = Vector2D.distance(current.getLocation(), location);
                if(distance < minDistance){
                    minDistance = distance;
                    nearest = current;
                }
            }
        return nearest;
    }

    public void loadBlankWorld(){
        mapSizeX = 1;
        mapSizeY = 1;
        this.spawnX = 0;
        this.spawnY = 0;
        pointLights = new ArrayList<>();
        interactiveObjects = new ArrayList<>();
        litObjects = new ArrayList<>();
        doors = new ArrayList<>();
        furnitures = new ArrayList<>();
        this.shadowMap = new ShadowMap(mapSizeX, mapSizeY, new Color(35, 35, 60));
        this.tileGrid = new Block[mapSizeY][mapSizeX];

        Block block = new Block(new Vector2D(0,0),0);
        tileGrid[0][0] = block;

        this.currentLevel = 0;
        navGrid = new NavGrid(parentWorld);
        shadowMap.drawShadowMap(parentWorld);
    }

    public void loadWorld(int level){
        try {

            String levelName = "lvl"+level+".txt";
            pointLights = new ArrayList<>();
            interactiveObjects = new ArrayList<>();
            litObjects = new ArrayList<>();
            doors = new ArrayList<>();
            furnitures = new ArrayList<>();

            File file = new File("res/levels/"+levelName);
            BufferedReader bf = new BufferedReader(new FileReader(file));

            int column = 0;
            String line;
            while ((line = bf.readLine()) != null) {
                String[] row = line.split(",");

                //Map size
                if(row[0].equals("m")){
                    this.mapSizeX = Integer.parseInt(row[1]);
                    this.mapSizeY = Integer.parseInt(row[2]);
                    this.shadowMap = new ShadowMap(mapSizeX, mapSizeY, new Color(35, 35, 60));
                    this.tileGrid = new Block[mapSizeY][mapSizeX];
                    continue;
                }

                //Spawn
                if(row[0].equals("s")){
                    this.spawnX = Integer.parseInt(row[1]);
                    this.spawnY = Integer.parseInt(row[2]);
                    continue;
                }

                //PointLights
                if(row[0].equals("p")){
                    Vector2D lightPosition = new Vector2D(Float.parseFloat(row[1]), Float.parseFloat(row[2]));
                    int r = Integer.parseInt(row[4]);
                    int g = Integer.parseInt(row[5]);
                    int b = Integer.parseInt(row[6]);
                    pointLights.add(new PointLight(lightPosition, Float.parseFloat(row[3]), new Color(r,g,b), parentWorld));
                    continue;
                }
                //Interactive objects

                //Computers
                if(row[0].equals("c")){
                    Vector2D position = new Vector2D(Float.parseFloat(row[1]), Float.parseFloat(row[2]));
                    String name = row[3];
                    int r = Integer.parseInt(row[4]);
                    int g = Integer.parseInt(row[5]);
                    int b = Integer.parseInt(row[6]);
                    Computer computer = new Computer(name, position, new Color(r,g,b), parentWorld.getParentEngine());
                    interactiveObjects.add(computer);
                    litObjects.add(computer);
                    continue;
                }

                //Doors
                if(row[0].equals("d")){
                    Vector2D position = new Vector2D(Float.parseFloat(row[1]), Float.parseFloat(row[2]));
                    Door door = new Door(position, parentWorld, doors.size());
                    interactiveObjects.add(door);
                    litObjects.add(door);
                    doors.add(door);
                    continue;
                }
                if(row[0].equals("fur")){
                    Vector2D position = new Vector2D(Float.parseFloat(row[2]), Float.parseFloat(row[3]));
                    int direction = Integer.parseInt(row[4]);
                    if(row[1].equals("couch")){
                        Couch couch = new Couch(position);
                        furnitures.add(couch);
                        litObjects.add(couch);
                        couch.setDirection(direction);
                        continue;
                    }
                    if(row[1].equals("chair")){
                        Chair_Kitchen chair_kitchen = new Chair_Kitchen(position);
                        furnitures.add(chair_kitchen);
                        litObjects.add(chair_kitchen);
                        chair_kitchen.setDirection(direction);
                        continue;
                    }
                }

                //Tiles
                int rowSize = row.length;
                for(int i=0; i<rowSize; i++){
                    Block block = new Block(new Vector2D(i, column), Integer.parseInt(row[i])-1);
                    tileGrid[column][i] = block;
                    litObjects.add(block);
                }
                column++;
            }
            bf.close();
            this.currentLevel = level;
            //Debug here
            navGrid = new NavGrid(parentWorld);


            //Lightning
            shadowMap.drawShadowMap(parentWorld);
            for(var light : pointLights){
                light.updateNearbyLitObjects();
                light.drawLight();
            }

        } catch (IOException e) {
            System.out.println("Error : Couldn't load the level");
        }
    }

    private int getUpTileIndex(Vector2D location, int radius){
        int result = (int)(location.getY()+1 - radius);
        if(result < 0)
            result = 0;
        return result;
    }
    private int getLeftTileIndex(Vector2D location, int radius){
        int result = (int)(location.getX()+1 - radius);
        if(result < 0)
            result = 0;
        return result;
    }
    private int getRightTileIndex(Vector2D location, int radius){
        int result = (int)(location.getX()+1 + radius);
        if(result > getMapSizeX()-1)
            result = getMapSizeX();
        return result;
    }
    private int getBottomTileIndex(Vector2D location, int radius){
        int result = (int)(location.getY()+1 + radius);
        if(result > getMapSizeY()-1)
            result = getMapSizeY();
        return result;
    }
}
