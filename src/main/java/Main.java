import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    static int frameCount = 0;

    public static void main(String[] args) throws IOException {
        VideoProcessor processor = new VideoProcessor("./ffmpeg/ffmpeg.exe", "./ffmpeg/ffprobe.exe");
        processor.videoToFrames("./input.mp4");

        System.out.print("[sofvfx] Processing frames...");
        File directory = new File("./frames/");
        File[] frames = directory.listFiles();

/*        assert frames != null;
        for (File frame : frames) {
            if (frame.isFile()) {
                File file = new File(frame.getPath());
                BufferedImage img = ImageIO.read(file);
                System.out.print("[sofvfx] Processing " + file.getName() + "\r");
                textToImage(EffectsProcessor.frameToText(img, 6), img.getHeight(), img.getWidth());
            }
        }*/
        for (File frame : frames) {
            if (frame.isFile()) {
                File file = new File(frame.getPath());
                BufferedImage img = ImageIO.read(file);
                System.out.print("[sofvfx] Processing " + file.getName() + "\r");

                // effect layers
//                img = EffectsProcessor.pixelsort(img, 1);
//                img = EffectsProcessor.shadeAscii(img, 6, false, true);
//                img = EffectsProcessor.pixelate(img, 6);

                ImageIO.write(img, "png", new File("./processed/" + ("00000".substring(0, (5 - Integer.toString(frameCount).length())) + frameCount) + ".png"));
                frameCount++;
            }
        }
        System.out.print("[sofvfx] Finished processing frames.\r\n");

        processor.framesToVideo("./output.avi");
    }

    public static void textToImage(ArrayList<String> lines, int height, int width) throws IOException {
        int scaling = 2;
        BufferedImage textImage = new BufferedImage(width * scaling, height * scaling, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = textImage.getGraphics();

        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, height * scaling, width * scaling);

        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Consolas", Font.BOLD, 8 * scaling));
        for (int i = 0; i < lines.size(); i++) {
            graphics.drawString(lines.get(i), 0, (8 * scaling) * i);
        }
        frameCount++;
        ImageIO.write(textImage, "png", new File("./processed/" + ("00000".substring(0, (5 - Integer.toString(frameCount).length())) + frameCount) + ".png"));
    }

}

