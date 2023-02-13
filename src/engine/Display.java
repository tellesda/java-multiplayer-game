package engine;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Dimension;


public class Display {
    private JFrame frame;
    private Canvas canvas;

    //Getter
    public Canvas getCanvas(){
        return canvas;
    }
    //Constructor
    public Display(int width, int height){
        createDisplay(width, height, false);
    }
    public JFrame getFrame(){
        return frame;
    }

    //Setup the display
    public void createDisplay(int width, int height, boolean fullScreen){
        if(frame != null)
            frame.dispose();
        frame = new JFrame("Game");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        if(fullScreen){
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);
        }
        frame.setVisible(true);


        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width,height));
        canvas.setMaximumSize(new Dimension(width,height));
        canvas.setMinimumSize(new Dimension(width,height));
        canvas.setFocusable(false);

        frame.add(canvas);
        frame.pack();
    }
}
