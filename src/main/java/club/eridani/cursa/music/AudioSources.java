package club.eridani.cursa.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;

public class AudioSources {
    public static void registerSources(AudioPlayerManager audioPlayerManager) {
        final YoutubeAudioSourceManager youtube = new YoutubeAudioSourceManager(true);
        youtube.setPlaylistPageCount(30);
        audioPlayerManager.registerSourceManager(youtube);
        audioPlayerManager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
    }
}
