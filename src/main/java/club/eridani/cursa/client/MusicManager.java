package club.eridani.cursa.client;

import club.eridani.cursa.music.AudioOutput;
import club.eridani.cursa.music.AudioSources;
import club.eridani.cursa.utils.MusicUtil;
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.Pcm16AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public class MusicManager extends AudioEventAdapter {
    public static MusicManager INSTANCE;

    private final AudioPlayerManager audioPlayerManager;
    private final AudioDataFormat audioDataFormat;
    private AudioPlayer audioPlazer;
    private final AudioOutput audioOutput;

    public static void init() {
        INSTANCE = new MusicManager();
    }

    public MusicManager() {
        audioPlayerManager = new DefaultAudioPlayerManager();
        audioDataFormat = new Pcm16AudioDataFormat(2, 48000, 960, true);
        audioPlazer = audioPlayerManager.createPlayer();
        audioOutput = new AudioOutput(this);

        audioPlayerManager.setFrameBufferDuration(1000);
        audioPlayerManager.setPlayerCleanupThreshold(Long.MAX_VALUE);

        audioPlayerManager.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
        audioPlayerManager.getConfiguration().setOpusEncodingQuality(10);
        audioPlayerManager.getConfiguration().setOutputFormat(audioDataFormat);

        AudioSources.registerSources(audioPlayerManager);

        startAudioOutput();
        setVolume(20);

        audioPlazer.addListener(this);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(!endReason.mayStartNext) return;
        MusicUtil.playTrack(track);
    }

    public AudioPlayerManager getAudioPlayerManager() {
        return audioPlayerManager;
    }

    public AudioDataFormat getAudioDataFormat() {
        return audioDataFormat;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlazer;
    }

    public void startAudioOutput() {
        audioOutput.start();
    }

    public void setVolume(int volume) {
        audioPlazer.setVolume(volume);
    }
}
