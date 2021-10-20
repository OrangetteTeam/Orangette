package club.eridani.cursa.utils;

import club.eridani.cursa.client.MusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.Objects;

public class MusicUtil {
    private static String yt = "ytsearch: ";
    private static String sc = "scsearch:  ";

    public static void searchYT(String word, final AudioLoadResultHandler handler) {
        getManager().loadItem(yt + word, handler);
    }

    public static void searchSC(String word, final AudioLoadResultHandler handler) {
        getManager().loadItem(sc + word, handler);
    }

    public static void playTrack(AudioTrack track) {
        getAudioPlayer().playTrack(track.makeClone());
    }

    public static float getProgress(){
        AudioTrack track = getAudioPlayer().getPlayingTrack();
        if(Objects.isNull(track)) return 0.0F;
        float d = track.getDuration() / 1000;
        float p = track.getPosition() / 1000;
        return p / d;
    }

    public static void setVolume(int volume) {
        getPlayer().setVolume(volume);
    }

    public static MusicManager getPlayer() {
        return MusicManager.INSTANCE;
    }

    public static AudioPlayerManager getManager() {
        return getPlayer().getAudioPlayerManager();
    }

    public static AudioPlayer getAudioPlayer() {
        return getPlayer().getAudioPlayer();
    }
}
