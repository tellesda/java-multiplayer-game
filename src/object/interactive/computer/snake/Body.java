package object.interactive.computer.snake;

import java.awt.Graphics2D;
import java.util.Stack;

public class Body {

    //Attributes
    private final Body parent;
    private Body child;
    private int x,y;
    private int prevX = -1, prevY = -1;

    public Body(Body parent, Body child){
        this.parent = parent;
        this.child = child;
    }

    public void setX(int x) {
        this.prevX = this.x;
        this.x = x;
    }

    public void setY(int y) {
        this.prevY = this.y;
        this.y = y;
    }

    public void setLocation(int x, int y){
        this.prevX = this.x;
        this.x = x;
        this.prevY = this.y;
        this.y = y;
    }

    public Body getChild() {
        return child;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static void addTail(Body body) {
        if(body.child == null){
            Body child = new Body(body, null);
            child.setX(body.x);
            child.setY(body.y);
            body.child = child;
        }
        else
            addTail(body.child);
    }

    public static void moveTail(Body body){
        if(body.parent != null){
            if(body.parent.prevX != -1){
                body.setX(body.parent.prevX);
                body.setY(body.parent.prevY);
            }
        }
        if(body.child != null)
            moveTail(body.child);
    }

    public static boolean collidesWithSnake(int x, int y, Body body){
        if(body.x == x && body.y == y)
            return true;
        if(body.child != null)
            return collidesWithSnake(x, y, body.child);
        return false;
    }

    public static Stack<Integer[]> getSnakeXYs(Body body){
        Stack<Integer[]> stack = new Stack<>();
        if(body.child == null){
            stack.add(new Integer[]{body.getX(), body.getY()});
        }
        else{
            stack.add(new Integer[]{body.getX(), body.getY()});
            stack.addAll(Body.getSnakeXYs(body.child));
        }
        return stack;
    }

    public static void drawSnake(Graphics2D g, Body body){
        g.fillRect(body.x, body.y, 1,1);
        if(body.child != null)
            drawSnake(g, body.child);
    }
}
