package object;

import math.Vector2D;
import scene.World;
import java.awt.Graphics;

public interface Sortable {

    Vector2D getLocation();

    float getY();

    void render(Graphics g, int width, int height, World world);

}
