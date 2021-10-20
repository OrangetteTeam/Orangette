package club.eridani.cursa.music;

import club.eridani.cursa.client.MusicManager;
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class AudioOutput extends Thread{
    private final MusicManager musicPlayer;

    public AudioOutput(MusicManager musicPlayer) {
        super("Audio Player");
        this.musicPlayer = musicPlayer;
    }

    @Override
    public void run() {
        try {
            AudioPlayer player = musicPlayer.getAudioPlayer();
            AudioDataFormat dataformat = musicPlayer.getAudioDataFormat();

            AudioInputStream stream = AudioPlayerInputStream.createStream(player, dataformat, dataformat.frameDuration(), true);

            SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, stream.getFormat());
            SourceDataLine output = (SourceDataLine) AudioSystem.getLine(info);

            int buffersize = dataformat.chunkSampleCount * dataformat.channelCount * 2;

            output.open(stream.getFormat(), buffersize * 5);
            output.start();

            byte[] buffer = new byte[buffersize];
            int chunkSize;
            long frameDuration = dataformat.frameDuration();
            while (true) {
                if (!player.isPaused()) {
                    if ((chunkSize = stream.read(buffer)) >= 0) {
                        output.write(buffer, 0, chunkSize);
                    } else {
                        throw new IllegalStateException("Audiostream ended. This should not happen.");
                    }
                } else {
                    output.drain();
                    sleep(frameDuration);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
