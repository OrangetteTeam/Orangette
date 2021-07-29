package club.eridani.cursa.concurrent.repeat;

import club.eridani.cursa.concurrent.task.EventTask;
import club.eridani.cursa.concurrent.task.VoidTask;
import club.eridani.cursa.concurrent.utils.Timer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntSupplier;

/**
 * Created by B_312 on 05/01/2021
 */
public class RepeatUnit {

    VoidTask task;

    private final AtomicBoolean runnable = new AtomicBoolean(true);
    private final AtomicBoolean isRunning = new AtomicBoolean(true);
    private final AtomicBoolean isDead = new AtomicBoolean(false);
    public IntSupplier delaySupplier = null;
    public int delay = -1;
    private final Timer timer = new Timer();
    private VoidTask timeOutOperation = null;
    private int times = 0;
    private boolean isDelayTask = false;
    private AfterTimeOutOperation afterTimeOut = AfterTimeOutOperation.NONE;

    public enum AfterTimeOutOperation {
        NONE,
        SUSPEND,
        STOP
    }

    public RepeatUnit(int delay, VoidTask task) {
        this.task = task;
        this.delay = delay;
        this.times += Integer.MAX_VALUE;
    }

    public RepeatUnit(int delay, int times, VoidTask task) {
        this.task = task;
        this.delay = delay;
        this.times += times;
    }

    public RepeatUnit(int delay, int times, boolean isDelayTask, VoidTask task) {
        this.task = task;
        this.delay = delay;
        this.times += times;
        this.isDelayTask = isDelayTask;
    }

    public RepeatUnit(IntSupplier delay, VoidTask task) {
        this.task = task;
        this.delaySupplier = delay;
        this.times += Integer.MAX_VALUE;
    }

    public RepeatUnit(IntSupplier delay, int times, VoidTask task) {
        this.task = task;
        this.delaySupplier = delay;
        this.times += times;
    }

    public RepeatUnit(int delay, VoidTask timeOutOperation, VoidTask task) {
        this.task = task;
        this.delay = delay;
        this.timeOutOperation = timeOutOperation;
        this.times += Integer.MAX_VALUE;
    }

    public RepeatUnit(int delay, int times, VoidTask timeOutOperation, VoidTask task) {
        this.task = task;
        this.delay = delay;
        this.timeOutOperation = timeOutOperation;
        this.times += times;
    }

    public RepeatUnit(IntSupplier delay, int times, VoidTask timeOutOperation, VoidTask task) {
        this.task = task;
        this.delaySupplier = delay;
        this.timeOutOperation = timeOutOperation;
        this.times += times;
    }

    public void run() {
        if (isDelayTask) {
            isDelayTask = false;
            return;
        }
        if (!runnable.get()) return;
        if (isRunning.get() && !isDead.get() && task != null) {
            if (times > 0) {
                runnable.set(false);
                try {
                    task.invoke();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                runnable.set(true);
                if (timeOutOperation != null && timer.passed(getDelay())) {
                    timeOutOperation.invoke();
                    switch (afterTimeOut) {
                        case SUSPEND: {
                            suspend();
                            break;
                        }
                        case STOP: {
                            stop();
                        }
                    }
                }
                times--;
            } else {
                stop();
            }
        }
    }

    public int getDelay() {
        return delaySupplier != null ? delaySupplier.getAsInt() : delay;
    }

    public RepeatUnit afterTimeOutOperation(AfterTimeOutOperation atop) {
        this.afterTimeOut = atop;
        return this;
    }

    public RepeatUnit timeOut(EventTask<RepeatUnit> task) {
        this.timeOutOperation = () -> {
            task.invoke(this);
        };
        return this;
    }

    public RepeatUnit timeOut(AfterTimeOutOperation atop, EventTask<RepeatUnit> task) {
        this.timeOutOperation = () -> {
            task.invoke(this);
        };
        this.afterTimeOut = atop;
        return this;
    }

    public boolean shouldRun() {
        if (timer.passed(getDelay())) {
            timer.reset();
            return true;
        } else return false;
    }

    public boolean isRunning() {
        return isRunning.get();
    }

    public RepeatUnit suspend() {
        isRunning.set(false);
        return this;
    }

    public void resume() {
        isRunning.set(true);
    }

    public synchronized void stop() {
        isDead.set(true);
        task = null;
        isRunning.set(false);
    }

    public boolean isDead() {
        return isDead.get();
    }

}

