package scene;

import debug.DebugPoint;
import engine.Engine;
import math.Vector2D;
import network.CommandLine;
import network.RequestHandler;
import network.packetClasses.OtherPlayerPacket;
import network.packetClasses.PlayerMovementPacket;
import object.*;
import object.ai.Npc;
import object.ai.TestNPC;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class World implements Scene{

    //Attributes
    private final Engine parentEngine;
    private CommandLine commandLine;
    private final Level level;
    private Camera camera;
    private Player player;
    private List<Npc> npcs;
    private final List<OtherPlayer> otherPlayers;
    private List<Sortable> sortableObjects;
    private List<DebugPoint> debugPoints;

    public World(Engine engine){
        this.parentEngine = engine;
        this.level = new Level(this);
        this.otherPlayers = new ArrayList<>();
        engine.getRequestHandler().setWorld(this);

        if(parentEngine.isServer)
            return;

        this.commandLine = new CommandLine(this);
    }

    public Camera getCamera() {
        return camera;
    }
    public Engine getParentEngine() {
        return parentEngine;
    }
    public List<Npc> getNpcs() {
        return npcs;
    }
    public List<Sortable> getSortableObjects() {
        return sortableObjects;
    }
    public List<DebugPoint> getDebugPoints() {
        return debugPoints;
    }
    public Level getLevel() {
        return level;
    }
    public CommandLine getCommandLine() {
        return commandLine;
    }
    public List<OtherPlayer> getOtherPlayers() {
        return otherPlayers;
    }
    public Player getPlayer() {
        return player;
    }

    public void setOtherPlayerLocation(PlayerMovementPacket packet){
        for(var otherPlayer : otherPlayers){
            if(otherPlayer.id == packet.id){
                float x = Engine.shortToFloat(packet.x);
                float y = Engine.shortToFloat(packet.y);
                otherPlayer.setLocation(new Vector2D(x,y));
                otherPlayer.setAnimationIndex(packet.animation);
                return;
            }
        }
    }

    public void addOtherPlayer(OtherPlayerPacket playerInfo){
        for(var otherPlayer : otherPlayers){
            if(otherPlayer.id == playerInfo.id)
                return;
        }
        OtherPlayer otherPlayer = new OtherPlayer(this);
        otherPlayer.name = playerInfo.name;
        otherPlayer.id = playerInfo.id;
        getOtherPlayers().add(otherPlayer);
        initSortableObjects();
    }

    public void removeOtherPlayer(int id){
        OtherPlayer otherPlayerToRemove = null;
        for(var otherPlayer : otherPlayers){
            if(id == otherPlayer.id){
                otherPlayerToRemove = otherPlayer;
            }
        }
        if(otherPlayerToRemove == null)
            return;

        otherPlayers.remove(otherPlayerToRemove);
        initSortableObjects();
    }

    //Index of 0 will load the blank level
    public void loadLevel(int levelIndex){
        level.isLoading = true;
        npcs = new ArrayList<>();
        debugPoints = new ArrayList<>();

        if(levelIndex == 0)
            level.loadBlankWorld();
        else
            level.loadWorld(levelIndex);

        int spawnX = level.getSpawnX();
        int spawnY = level.getSpawnY();

        if(!parentEngine.isServer){
            player.setLocation(new Vector2D(spawnX, spawnY));
            camera.setLocation(new Vector2D(spawnX, spawnY));
        }

        npcs.add(new TestNPC(new Vector2D(spawnX+2, spawnY-4), this));

        initSortableObjects();
        level.isLoading = false;
    }

    public void init() {
        if(parentEngine.isServer){
            loadLevel(3);
        }
        else{
            camera = new Camera(new Vector2D(0f, 0f), this);
            player = new Player(new Vector2D(0f, 0f), this);
            loadLevel(2);
            commandLine.init();
        }

    }

    private void initSortableObjects(){
        sortableObjects = new ArrayList<>();
        sortableObjects.add(player);
        sortableObjects.addAll(otherPlayers);
        sortableObjects.addAll(level.getInteractiveObjects());
        sortableObjects.addAll(npcs);
        sortableObjects.addAll(level.getFurniture());
    }

    public void tick() {
        if(!parentEngine.isServer){
            commandLine.tick();
            player.tick();
        }
        else{
            //Send movement info of all OtherPlayers to all clients
            parentEngine.gameServer.sendMovementInfo();
        }

        for(var npc : npcs)
            npc.tick();

        for(var otherPlayer : otherPlayers)
            otherPlayer.tick();

        debugPoints.removeIf(debugPoint -> debugPoint.frames > debugPoint.maxFrames);
    }

    public void render(Graphics g) {

        int width = parentEngine.width;
        int height = parentEngine.height;

        //Draw background
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);

        if(level.isLoading)
            return;

        //Indexes of the first invisible blocks
        int startX = camera.getLeftTileIndex();
        int endX = camera.getRightTileIndex();
        int startY = camera.getUpTileIndex();
        int endY = camera.getBottomTileIndex();

        for(int m = startY; m<endY; m++)
            for(int n = startX; n<endX; n++){
                Block tile = level.getTileGrid()[m][n];
                tile.render(g, width, height, this);
            }


        List<Sortable> sortableToRender = new ArrayList<>();

        for(var sortable : sortableObjects){

            int locX = (int)sortable.getLocation().getX();
            int locY = (int)sortable.getLocation().getY();

            boolean isVisible = locX<=endX && locX>=startX && locY<=endY && locY>=startY;

            if(isVisible)
                sortableToRender.add(sortable);
        }

        sortableToRender.sort(Comparator.comparing(Sortable::getY));

        for(var sortable : sortableToRender) {
            sortable.render(g, width, height, this);
        }

        if(player.getUiScene() != null){
            player.getUiScene().render(g);
        }

        for(var debugPoint : debugPoints)
            debugPoint.render(g, width, height, this);

        commandLine.render(g);
    }
}
