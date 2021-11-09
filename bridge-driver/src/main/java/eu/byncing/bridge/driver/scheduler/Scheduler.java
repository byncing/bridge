package eu.byncing.bridge.driver.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {

    private static final List<SchedulerFuture> futures = new ArrayList<>();

    private static final Timer timer = new Timer();

    public static void schedule(Runnable runnable) {
        SchedulerFuture future = new SchedulerFuture(futures.size(), runnable);
        new Thread(runnable).start();
    }

    public static SchedulerFuture schedule(Runnable runnable, long delay, long period) {
        SchedulerFuture future = new SchedulerFuture(futures.size(), runnable);
        futures.add(future);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!future.isRunning()) {
                    cancel();
                    cancelFuture(future);
                    return;
                }
                future.run();
            }
        }, delay, period);
        return future;
    }

    public static SchedulerFuture schedule(Runnable runnable, long delay) {
        SchedulerFuture future = new SchedulerFuture(futures.size(), runnable);
        futures.add(future);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                future.run();
                cancel();
                cancelFuture(future);
            }
        }, delay);
        return future;
    }

    public static void cancelFuture(SchedulerFuture future) {
        futures.remove(future);
        future.cancel();
        //if (futures.size() == 0) timer.cancel();
    }

    public static void cancelFuture(int id) {
        SchedulerFuture schedulerFuture = futures.stream().filter(future -> future.getId() == id).findFirst().orElse(null);
        cancelFuture(schedulerFuture);
    }

    public static void cancels() {
        futures.forEach(SchedulerFuture::cancel);
        timer.cancel();
    }

    public static List<SchedulerFuture> getFutures() {
        return futures;
    }

    public static Timer getTimer() {
        return timer;
    }
}