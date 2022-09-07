import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class VideoProcessor {

    FFmpeg ffmpeg;
    FFprobe ffprobe;

    public VideoProcessor(String ffmpeg, String ffprobe) throws IOException {
        this.ffmpeg = new FFmpeg(ffmpeg);
        this.ffprobe = new FFprobe(ffprobe);

        FileUtils.cleanDirectory(new File("./frames/"));
        FileUtils.cleanDirectory(new File("./processed/"));
        System.out.println("[sofvfx] Cleaned utility directories.");
    }

    public void videoToFrames(String input) throws IOException {
        System.out.print("[sofvfx] Splitting video into frames...");
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(input)
                .addOutput("./frames/frame%03d.png")
                .setVideoFrameRate(FFmpeg.FPS_23_976)
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
        System.out.println("Split video into frames.\r");
    }

    public void framesToVideo(String output) throws IOException {
        System.out.print("[sofvfx] Merging frames into video...");
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput("./processed/%03d.png")
                .addOutput(output)
                .setVideoFrameRate(FFmpeg.FPS_23_976)
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
        System.out.println("[sofvfx] Frames merged.\r");
    }
}
