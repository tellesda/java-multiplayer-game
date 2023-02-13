package anim;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class ImageReader {

    public static BufferedImage readImage(String path) {

        try {
            return ImageIO.read(ImageReader.class.getResource(path));
        }
        catch (IOException e) {
            System.err.println("Could not load png file.");
            System.exit(1);
        }
        return null;
        }

}
