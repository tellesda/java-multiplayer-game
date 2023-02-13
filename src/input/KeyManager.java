package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {

    private final boolean[] keys;
    public boolean keyW,keyS,keyA,keyD,keyF1,keyF2,keyE,keyESC, keyENTER;
    public boolean up, down, left, right, space, lshift;

    public StringBuilder text = new StringBuilder();
    public boolean canType;

    public KeyManager(){
        keys = new boolean[256];
    }

    public void tick(){
        keyW = keys[KeyEvent.VK_W];
        keyS = keys[KeyEvent.VK_S];
        keyA = keys[KeyEvent.VK_A];
        keyD = keys[KeyEvent.VK_D];
        keyE = keys[KeyEvent.VK_E];
        keyESC = keys[KeyEvent.VK_ESCAPE];
        keyENTER = keys[KeyEvent.VK_ENTER];

        up = keys[KeyEvent.VK_UP];
        down = keys[KeyEvent.VK_DOWN];
        left = keys[KeyEvent.VK_LEFT];
        right = keys[KeyEvent.VK_RIGHT];
        space = keys[KeyEvent.VK_SPACE];
        lshift = keys[KeyEvent.VK_SHIFT];

        keyF1 = keys[KeyEvent.VK_F1];
        keyF2 = keys[KeyEvent.VK_F2];
    }

    public void resetText(){
        text = new StringBuilder();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() > 255)
            return;
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(canType){
            char c = e.getKeyChar();
            if(c != KeyEvent.VK_ENTER && c != KeyEvent.VK_ESCAPE)
                return;
        }
        if(e.getKeyCode() > 255)
            return;
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar() == KeyEvent.VK_ESCAPE)
            return;
        if(e.getKeyChar() == KeyEvent.VK_ENTER)
            return;
        if(e.getKeyChar() == KeyEvent.VK_BACK_SPACE){
            if(!text.isEmpty())
                text.deleteCharAt(text.length()-1);
        }
        else if(canType)
            text.append(e.getKeyChar());
    }
}
