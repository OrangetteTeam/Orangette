package club.eridani.cursa.event.events.player;

import club.eridani.cursa.event.CursaEvent;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class PlayerDamageBlockEvent extends CursaEvent {
    public BlockPos pos;
    public EnumFacing facing;
    public PlayerDamageBlockEvent(BlockPos pos , EnumFacing facing) {
        this.pos = pos;
        this.facing = facing;
    }
}
