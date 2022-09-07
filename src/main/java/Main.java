import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedImage img = ImageIO.read(new File("test.jpg"));
        String shades = "@$#*!=;:~,. ";

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                int rgb = img.getRGB(i, j);
                int red =   (rgb & 0x00ff0000) >> 16;
                int green = (rgb & 0x0000ff00) >> 8;
                int blue =   rgb & 0x000000ff;
                System.out.print(shades.toCharArray()[((red + green + blue) / 3) % shades.length()]);
            }
            System.out.print("\n");
        }
    }
}