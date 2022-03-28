package club.eridani.cursa.mixin.mixins.render;

import club.eridani.cursa.mixin.mixins.accessor.AccessorSplashProgress;
import club.eridani.cursa.utils.ColorUtil;
import club.eridani.cursa.utils.RenderUtil;
import club.eridani.cursa.utils.SplashTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.SplashProgress;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.ProgressManager;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Iterator;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

import static org.lwjgl.opengl.GL11.*;

@Mixin(value = SplashProgress.class)
public class MixinSplashProgress {
    /*
    private static volatile boolean pause = false;
    private static volatile boolean done = false;
    private static int backgroundColor;
    private static final int TIMING_FRAME_COUNT = 200;
    private static final int TIMING_FRAME_THRESHOLD = TIMING_FRAME_COUNT * 5 * 1000000;
    private static boolean isDisplayVSyncForced = false;

    @Inject(method = "finish()V", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/client/SplashProgress$Texture;delete()V"), cancellable = true, remap = false)
    private static void finish(CallbackInfo ci) {
        //fontTexture etc is null because it overwrote the thread. so we have to cancel the finish method before fontTexuture executes delete.
        ci.cancel();
    }

    @Inject(method = "start()V", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;setUncaughtExceptionHandler(Ljava/lang/Thread$UncaughtExceptionHandler;)V"), remap = false)
    private static void setUncaughtExceptionHandler(CallbackInfo ci) {
        backgroundColor = ColorUtil.toRGBA(255, 255, 255);

        Thread t = new Thread(new Runnable() {
            private long framecount;
            private long updateTiming;

            @Override
            public void run() {
                setGL();
                SplashTexture background = new SplashTexture("/assets/minecraft/orangette/background/splashImage.png", null);
                SplashTexture bar0 = new SplashTexture("/assets/minecraft/orangette/background/bar/bar0.png", null);
                SplashTexture bar1 = new SplashTexture("/assets/minecraft/orangette/background/bar/bar1.png", null);

                Semaphore mutex = AccessorSplashProgress.getMutex();
                glDisable(GL_TEXTURE_2D);
                while (!done) {
                    //update
                    done = AccessorSplashProgress.getDone();
                    pause = AccessorSplashProgress.getPause();

                    framecount++;
                    ProgressManager.ProgressBar first = null, penult = null, last = null;
                    Iterator<ProgressManager.ProgressBar> i0 = ProgressManager.barIterator();
                    while (i0.hasNext()) {
                        if (first == null) first = i0.next();
                        else {
                            penult = last;
                            last = i0.next();
                        }
                    }

                    glClear(GL_COLOR_BUFFER_BIT);

                    // matrix setup
                    int w = Display.getWidth();
                    int h = Display.getHeight();
                    glViewport(0, 0, w, h);
                    glMatrixMode(GL_PROJECTION);
                    glLoadIdentity();
                    glOrtho(320 - w / 2, 320 + w / 2, 240 + h / 2, 240 - h / 2, -1, 1);
                    glMatrixMode(GL_MODELVIEW);
                    glLoadIdentity();

                    w = (int) (w / 2);
                    h = (int) (h / 2);

                    float p, p0, p1;
                    //futao
                    p0 = w / 1400.0F;
                    p1 = h / 788.0F;
                    p = Math.max(p0, p1);
                    drawTexture(background, 0, 0, 1400 * p, 788 * p, new Color(255, 255, 255));

                    //progressbar
                    float barScale = 0.12F;
                    p0 = w / 3565.0F;
                    p1 = h / 449.0F;
                    p = Math.max(p0, p1);
                    drawTexture(bar0, 0, (h - (449 * barScale) - 20), (3565 * barScale) * p, (449 * barScale) * p, new Color(255, 255, 255));
                    //progress
                    float progress = 0.0F;
                    try {
                        progress = first.getStep() * 1.0F / first.getSteps();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    scissor(0 , 0 , (3565 * barScale) * p * progress , 9999 , 2);
                    glEnable(GL_SCISSOR_TEST);
                    drawTexture(bar1, 0, (h - (449 * barScale) - 20), (3565 * barScale) * p, (449 * barScale) * p, new Color(255, 255, 255));
                    glDisable(GL_SCISSOR_TEST);

                    mutex.acquireUninterruptibly();
                    Display.update();
                    mutex.release();

                    mutex.acquireUninterruptibly();
                    long updateStart = System.nanoTime();
                    long dur = System.nanoTime() - updateStart;
                    if (framecount < TIMING_FRAME_COUNT) {
                        updateTiming += dur;
                    }
                    mutex.release();
                    if (pause) {
                        clearGL();
                        setGL();
                    }

                    if (framecount >= TIMING_FRAME_COUNT && updateTiming > TIMING_FRAME_THRESHOLD) {
                        if (!isDisplayVSyncForced) {
                            isDisplayVSyncForced = true;
                            FMLLog.log.info("Using alternative sync timing : {} frames of Display.update took {} nanos", TIMING_FRAME_COUNT, updateTiming);
                        }
                        try {
                            Thread.sleep(16);
                        } catch (InterruptedException ie) {
                        }
                    } else {
                        if (framecount == TIMING_FRAME_COUNT) {
                            FMLLog.log.info("Using sync timing. {} frames of Display.update took {} nanos", TIMING_FRAME_COUNT, updateTiming);
                        }
                        Display.sync(100);
                    }
                }
                background.delete();
                bar0.delete();
                bar1.delete();
                clearGL();
            }

            private void setColor(int color) {
                glColor3ub((byte) ((color >> 16) & 0xFF), (byte) ((color >> 8) & 0xFF), (byte) (color & 0xFF));
            }

            private void drawBox(int w, int h) {
                glBegin(GL_QUADS);
                glVertex2f(0, 0);
                glVertex2f(0, h);
                glVertex2f(w, h);
                glVertex2f(w, 0);
                glEnd();
            }

            private void setGL() {
                Lock lock = AccessorSplashProgress.getLock();
                lock.lock();
                try {
                    Display.getDrawable().makeCurrent();
                } catch (LWJGLException e) {
                    FMLLog.log.error("Error setting GL context:", e);
                    throw new RuntimeException(e);
                }
                glClearColor((float) ((backgroundColor >> 16) & 0xFF) / 0xFF, (float) ((backgroundColor >> 8) & 0xFF) / 0xFF, (float) (backgroundColor & 0xFF) / 0xFF, 1);
                glDisable(GL_LIGHTING);
                glDisable(GL_DEPTH_TEST);
                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            }

            private void clearGL() {
                Lock lock = AccessorSplashProgress.getLock();
                Minecraft mc = Minecraft.getMinecraft();
                mc.displayWidth = Display.getWidth();
                mc.displayHeight = Display.getHeight();
                mc.resize(mc.displayWidth, mc.displayHeight);
                glClearColor(1, 1, 1, 1);
                glEnable(GL_DEPTH_TEST);
                glDepthFunc(GL_LEQUAL);
                glEnable(GL_ALPHA_TEST);
                glAlphaFunc(GL_GREATER, .1f);
                try {
                    Display.getDrawable().releaseContext();
                } catch (LWJGLException e) {
                    FMLLog.log.error("Error releasing GL context:", e);
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }

            }
        });
        AccessorSplashProgress.setThread(t);
    }

    private static void drawTexture(SplashTexture image, float posX, float posY, float width, float height, Color color) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GlStateManager.enableBlend();
        GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
        image.bind();
        GL11.glTranslatef(posX, posY, 0.0f);
        glBegin(GL_QUADS);
        image.texCoord(0, 0, 0);
        glVertex2f(320 - width, 240 - height);
        image.texCoord(0, 0, 1);
        glVertex2f(320 - width, 240 + height);
        image.texCoord(0, 1, 1);
        glVertex2f(320 + width, 240 + height);
        image.texCoord(0, 1, 0);
        glVertex2f(320 + width, 240 - height);
        glEnd();
        GL11.glPopMatrix();
    }

    private static void drawRect(float posX, float posY, float width, float height, Color color) {
        GL11.glPushMatrix();
        GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
        GL11.glTranslatef(posX, posY, 0.0f);
        glBegin(GL_QUADS);
        glVertex2f(320 - width, 240 - height);
        glVertex2f(320 - width, 240 + height);
        glVertex2f(320 + width, 240 + height);
        glVertex2f(320 + width, 240 - height);
        glEnd();
        GL11.glPopMatrix();
    }

    private static void scissor(float x, float y, float width, float height, float guiScale) {
        float p = Display.getWidth() / 854.0F;
        GL11.glScissor(0, (int) (Display.getHeight() - (y + height) * p), (int) ((width + x) * p * guiScale), (int) (height * p));
    }

     */
}
