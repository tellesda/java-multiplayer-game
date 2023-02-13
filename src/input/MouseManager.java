package input;

import math.Vector2D;
import object.Camera;
import scene.World;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseManager implements MouseListener, MouseMotionListener {

    private boolean leftPressed, rightPressed;
    private int mouseX, mouseY;
    public boolean canClick;

    public MouseManager(){}

    // Getters

    public boolean isLeftPressed(){
        return leftPressed;
    }

    public boolean isRightPressed(){
        return rightPressed;
    }

    public int getMouseX(){
        return mouseX;
    }

    public int getMouseY(){
        return mouseY;
    }

    public Vector2D getClickWorldLocation(World world){
        int width = world.getParentEngine().width;
        int height = world.getParentEngine().height;
        Camera camera = world.getCamera();
        float cameraScaling = 64/camera.getScaling();

        Vector2D sized = new Vector2D(mouseX, mouseY);

        Vector2D camSpace = Vector2D.div(Vector2D.sub(sized, new Vector2D(width*0.5f, height*0.5f)),new Vector2D(cameraScaling, cameraScaling));
        Vector2D worldSpace = Vector2D.sub(camSpace, camera.getInversePosition());

        return Vector2D.add(worldSpace, new Vector2D(0.5f, 0.5f));
    }

    // Implemented methods

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1)
            leftPressed = true;
        else if(e.getButton() == MouseEvent.BUTTON3)
            rightPressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1)
            leftPressed = false;
        else if(e.getButton() == MouseEvent.BUTTON3)
            rightPressed = false;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

}
