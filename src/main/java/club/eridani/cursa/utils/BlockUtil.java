package club.eridani.cursa.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public class BlockUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static Block getBlock(BlockPos pos) {
        return getBlockState(pos).getBlock();
    }

    public static IBlockState getBlockState(BlockPos pos) {
        return mc.world.getBlockState(pos);
    }

    public static boolean canBreak(BlockPos pos) {
        IBlockState blockState = mc.world.getBlockState(pos);
        Block block = blockState.getBlock();
        return block.getBlockHardness(blockState, mc.world, pos) != -1.0f;
    }
}
