import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class VideoProcessor {

    FFmpeg ffmpeg;
    FFprobe ffprobe;
    FFmpegExecutor executor;
    public VideoProcessor(String ffmpeg, String ffprobe) throws IOException {
        this.ffmpeg = new FFmpeg(ffmpeg);
        this.ffprobe = new FFprobe(ffprobe);
        executor = new FFmpegExecutor(this.ffmpeg, this.ffprobe);

        FileUtils.cleanDirectory(new File("./frames/"));
        FileUtils.cleanDirectory(new File("./processed/"));
        System.out.println("[sofvfx] Cleaned utility directories.");
    }

    public void videoToFrames(String input) throws IOException {
        System.out.println("[sofvfx] Splitting video into frames...");
        FFmpegProbeResult in = ffprobe.probe(input);
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(in)
                .addOutput("./frames/frame%05d.png")
                .setVideoFrameRate(FFmpeg.FPS_23_976)
                .done();

        FFmpegJob job = executor.createJob(builder, new ProgressListener() {
            final double duration_ns = in.getFormat().duration * TimeUnit.SECONDS.toNanos(1);
            @Override
            public void progress(Progress progress) {
                double percentage = progress.out_time_ns / duration_ns;
                System.out.printf(
                        "[sofvfx] Splitting frames (%.0f%%) \r",
                        percentage * 100
                );
            }
        });
        job.run();
        System.out.println("[sofvfx] Split video into frames.\r");
    }

    public void framesToVideo(String output) throws IOException {
        System.out.print("[sofvfx] Merging frames into video...");
        FFmpegProbeResult in = ffprobe.probe("./processed/%05d.png");
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(in)
                .addOutput(output)
                .setVideoFrameRate(FFmpeg.FPS_23_976)
                .setVideoBitRate(300000000)
                .done();

        FFmpegJob job = executor.createJob(builder, new ProgressListener() {
            final double duration_ns = in.getFormat().duration * TimeUnit.SECONDS.toNanos(1);
            @Override
            public void progress(Progress progress) {
                double percentage = progress.out_time_ns / duration_ns;
                System.out.printf(
                        "[sofvfx] Merging frames (%.0f%%) \r",
                        percentage * 100
                );
            }
        });
        job.run();
        System.out.println("[sofvfx] Frames merged.\r");
    }
}
