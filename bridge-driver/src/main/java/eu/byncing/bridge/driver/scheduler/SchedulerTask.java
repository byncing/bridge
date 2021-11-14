package eu.byncing.bridge.driver.scheduler;

public class SchedulerTask {

    private boolean running = true;

    private final int id;
    private final Runnable runnable;

    public SchedulerTask(int id, Runnable runnable) {
        this.id = id;
        this.runnable = runnable;
    }

    public void run() {
        runnable.run();
    }

    public void cancel() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public int getId() {
        return id;
    }

    public Runnable getRunnable() {
        return runnable;
    }
}