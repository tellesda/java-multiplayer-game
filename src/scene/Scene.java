package scene;

import java.awt.Graphics;

public interface Scene {

    void init();
    void tick();
    void render(Graphics g);
}
