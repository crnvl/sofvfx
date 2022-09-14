import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

public class EffectsProcessor {
    public static ArrayList<String> frameToText(BufferedImage img, int reduce) {
        String shades = StringUtils.reverse("Ñ#W$9876543210?abc+=-_  ");

        ArrayList<String> lines = new ArrayList<String>();
        for (int i = 0; i < img.getHeight(); i += reduce * 1.4) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < img.getWidth(); j += reduce * 1.5) {
                int rgb = img.getRGB(j, i);
                int red = (rgb & 0x00ff0000) >> 16;
                int green = (rgb & 0x0000ff00) >> 8;
                int blue = rgb & 0x000000ff;

                String sign = Character.toString(shades.toCharArray()[((red + green + blue) / 3) / shades.length() % shades.length()]);
                line.append(sign);
                line.append(" ");
            }
            lines.add(String.valueOf(line));
        }
        return lines;
    }

    public static void coloredFrames(BufferedImage img, int reduce, int frameCount) throws IOException {
        BufferedImage newImg = new BufferedImage(img.getWidth() * reduce, img.getHeight() * reduce, BufferedImage.TYPE_INT_RGB);
        Graphics g = newImg.getGraphics();
        String shades = StringUtils.reverse("Ñ#W$9876543210?abc+=-_ ");
        String colorShades = StringUtils.reverse("AB");

        /*
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                g.setColor(new Color(img.getRGB(x, y)));
                g.fillRect(x * reduce, y * reduce, reduce, reduce);
            }
        }
*/
        g.setFont(new Font("Consolas", Font.BOLD, reduce * reduce * 2));
        for (int x = 0; x < img.getWidth(); x += reduce * 2 * 1.4) {
            for (int y = 0; y < img.getHeight(); y += reduce * 2 * 1.5) {
                int rgb = img.getRGB(x, y);
                int red = (rgb & 0x00ff0000) >> 16;
                int green = (rgb & 0x0000ff00) >> 8;
                int blue = rgb & 0x000000ff;

                g.setColor(new Color(red, green, blue));
                //g.setColor(fromShade(Character.toString(colorShades.toCharArray()[((red + green + blue) / 3) / colorShades.length() % colorShades.length()]), (red + green + blue) / 3));
                g.drawString(Character.toString(shades.toCharArray()[((red + green + blue) / 3) % shades.length()]), x * reduce, y * reduce);
            }
        }
        ImageIO.write(newImg, "png", new File("./processed/" + ("00000".substring(0, (5 - Integer.toString(frameCount).length())) + frameCount) + ".png"));
    }

    private static Color fromShade(String colorIndicator, int light) {
        return switch (colorIndicator) {
            case "B" -> new Color(239 * light % 255, 0, 12 * light % 255);
            case "A" -> new Color(0 * light % 255, 0, 101 * light % 255);
            default -> Color.BLACK;
        };
    }

    private static int minMaxNeon(int value) {
        return 255 / 2 > value ? 24 : 112;
    }
}
