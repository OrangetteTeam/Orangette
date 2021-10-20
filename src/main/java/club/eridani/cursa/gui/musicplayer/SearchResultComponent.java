package club.eridani.cursa.gui.musicplayer;

import club.eridani.cursa.gui.sigma.Component;
import club.eridani.cursa.utils.ColorUtil;
import club.eridani.cursa.utils.MusicUtil;
import club.eridani.cursa.utils.RenderUtil;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.awt.*;
import java.util.Objects;

public class SearchResultComponent extends Component {
    private int offset;
    private AudioTrack track;
    private boolean playing;

    public SearchResultComponent(AudioTrack track) {
        this.track = track;
        this.width = 200;
        this.height = 20;
    }

    @Override
    public int doRender(int x, int y, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        if (playing) RenderUtil.drawRect(x - 5, y, x + width, y + height, new Color(6, 167, 255, 255));
        fontRenderer.drawString(track.getInfo().title, this.x + offset, getCenter(y, height, fontRenderer.FONT_HEIGHT), playing ? ColorUtil.toRGBA(255, 255, 255, 255) : ColorUtil.toRGBA(0, 0, 0, 255), false);
        if (isMouseHovering(mouseX, mouseY)){
            scroll();
            RenderUtil.drawRect(x - 5, y, x + width, y + height, new Color(190, 190, 190, 60));
        }
        else{
            offset = 0;
        }
        AudioTrack playingTrack = MusicUtil.getAudioPlayer().getPlayingTrack();
        if (Objects.isNull(playingTrack)) playing = false;
        else playing = Objects.equals(playingTrack.getInfo().uri, track.getInfo().uri);

        return this.height;
    }

    public void scroll(){
        float w = fontRenderer.getStringWidth(track.getInfo().title);
        if(w > width){
            offset -= 1;
            if(w + offset + 5 < 0)
                offset = (int) (w);
        }
        else{
            offset = 0;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseHovering(mouseX, mouseY)) {
            MusicUtil.playTrack(track);
            playing = true;
        }
    }
}
