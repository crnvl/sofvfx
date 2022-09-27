import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
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

    public static BufferedImage shadeAscii(BufferedImage img, int reduce, boolean withOrigin, boolean colored) throws IOException {
        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = newImg.getGraphics();
        String shades = StringUtils.reverse("Ñ#W$9876543210?abc+=-_ ");
        String colorShades = StringUtils.reverse("AB");

        if (withOrigin) {
            for (int x = 0; x < img.getWidth(); x += reduce) {
                for (int y = 0; y < img.getHeight(); y += reduce) {
                    g.setColor(new Color(img.getRGB(x, y)));
                    g.fillRect(x, y, reduce + reduce, reduce + reduce);
                }
            }
        }
//
//        g.setColor(new Color(54, 57, 63));
//        g.fillRect(0, 0, img.getWidth(), img.getHeight());

        g.setFont(new Font("Consolas", Font.BOLD, reduce * 4));
        for (int x = 0; x < img.getWidth(); x += reduce * 2 * 1.4) {
            for (int y = 0; y < img.getHeight(); y += reduce * 2 * 1.5) {
                int rgb = img.getRGB(x, y);
                int red = (rgb & 0x00ff0000) >> 16;
                int green = (rgb & 0x0000ff00) >> 8;
                int blue = rgb & 0x000000ff;

                if(colored)
                    g.setColor(new Color(red, green, blue));
                else
                    g.setColor(fromShade(Character.toString(colorShades.toCharArray()[((red + green + blue) / 3) / colorShades.length() % colorShades.length()]), (red + green + blue) / 3));
                g.drawString(Character.toString(shades.toCharArray()[((red + green + blue) / 3) / shades.length() % shades.length()]), x + reduce, y + reduce);
            }
        }
        return newImg;
    }

    public static BufferedImage pixelate(BufferedImage img, int reduce) throws IOException {
        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = newImg.getGraphics();
        String shades = StringUtils.reverse("Ñ#W$9876543210?abc+=-_ ");
        String colorShades = StringUtils.reverse("AB");

        for (int x = 0; x < img.getWidth(); x += reduce) {
            for (int y = 0; y < img.getHeight(); y += reduce) {
                g.setColor(new Color(img.getRGB(x, y)));
                g.fillRect(x, y, reduce + reduce, reduce + reduce);
            }
        }
        return newImg;
    }

    public static BufferedImage pixelsort(BufferedImage img, int reduce) throws IOException {
        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = newImg.getGraphics();

//        for (int x = 0; x < img.getWidth(); x += reduce) {
//            for (int y = 0; y < img.getHeight(); y += reduce) {
//                g.setColor(new Color(img.getRGB(x, y)));
//                g.fillRect(x, y, reduce + reduce, reduce + reduce);
//            }
//        }

        for (int y = 0; y < img.getHeight(); y++) {
            int[] row = new int[img.getWidth()];
            for (int x = 0; x < img.getWidth(); x++) {
                row[x] = img.getRGB(x, y);
            }
            QuickSort.sort(row);

            for (int x = 0; x < img.getWidth(); x++) {
                int rgb = row[x];
                int red = (rgb & 0x00ff0000) >> 16;
                int green = (rgb & 0x0000ff00) >> 8;
                int blue = rgb & 0x000000ff;

                g.setColor(new Color(red, green, blue));
                g.fillRect(x, y, reduce + reduce, reduce + reduce);
            }
        }
        return newImg;
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
