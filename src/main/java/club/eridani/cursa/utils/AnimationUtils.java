package club.eridani.cursa.utils;

public class AnimationUtils {

    private static final float defaultAnimationSpeed = 0.6f;

    public static float animate(final float target, float current) {
        if (Math.abs(current - target) < defaultAnimationSpeed) {
            return current;
        }
        float dif = Math.max(target, current) - Math.min(target, current);
        float factor = dif * defaultAnimationSpeed;
        if (factor < 0.1f) {
            factor = 0.1f;
        }
        if (target > current) {
            current += factor;
        } else {
            current -= factor;
        }
        return current;
    }
}
