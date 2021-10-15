package club.eridani.cursa.utils;

import club.eridani.cursa.utils.math.Vec2i;
import net.minecraft.client.gui.ScaledResolution;

public class RenderHelper {

    public static Vec2i getStart(ScaledResolution scaledResolution, String caseIn) {
        switch (caseIn) {
            case "RightDown": {
                return new Vec2i(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
            }
            case "LeftTop": {
                return Vec2i.ZERO;
            }
            case "LeftDown": {
                return new Vec2i(0, scaledResolution.getScaledHeight());
            }
            default: {
                return new Vec2i(scaledResolution.getScaledWidth(), 0);
            }
        }
    }

    public static Vec2i getStart(ScaledResolution scaledResolution, StartPos caseIn) {
        switch (caseIn) {
            case RightDown: {
                return new Vec2i(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
            }
            case LeftTop: {
                return Vec2i.ZERO;
            }
            case LeftDown: {
                return new Vec2i(0, scaledResolution.getScaledHeight());
            }
            default: {
                return new Vec2i(scaledResolution.getScaledWidth(), 0);
            }
        }
    }

    enum StartPos {
        RightDown,
        RightTop,
        LeftDown,
        LeftTop
    }
}
