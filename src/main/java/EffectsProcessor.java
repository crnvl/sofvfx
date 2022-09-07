import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EffectsProcessor {
    public static ArrayList<String> frameToText(BufferedImage img, int reduce) {
        String shades = "  .,~:;=!*#$@";

        ArrayList<String> lines = new ArrayList<String>();
        for (int i = 0; i < img.getHeight(); i += reduce * 1.4) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < img.getWidth(); j += reduce) {
                int rgb = img.getRGB(j, i);
                int red =   (rgb & 0x00ff0000) >> 16;
                int green = (rgb & 0x0000ff00) >> 8;
                int blue =   rgb & 0x000000ff;
                line.append(shades.toCharArray()[((red + green + blue) / 3) / shades.length() % shades.length()]).append("  ");
            }
            lines.add(String.valueOf(line));
        }
        return lines;
    }
}
