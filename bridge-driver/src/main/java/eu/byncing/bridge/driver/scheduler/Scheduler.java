package eu.byncing.bridge.driver.scheduler;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    private final List<SchedulerTask> tasks = new ArrayList<>();

    public SchedulerTask runAsync(Runnable runnable) {
        SchedulerTask task = new SchedulerTask(tasks.size(), runnable);
        task.run();
        cancel(task.getId());
        return task;
    }

    public SchedulerTask runTimer(Runnable runnable, int delay, int period) {
        return run(new SchedulerTask(tasks.size(), runnable), delay, period);
    }

    public SchedulerTask runDelay(Runnable runnable, int delay) {
        return run(new SchedulerTask(tasks.size(), runnable), delay, 0);
    }

    public SchedulerTask run(SchedulerTask task, int delay, int period) {
        new Thread(() -> {
            long current = System.currentTimeMillis();

            tasks.add(task);

            while (task.isRunning()) {
                if (System.currentTimeMillis() - current > delay) {

                    if (period <= 0) {
                        if (task.isRunning()) task.run();
                        return;
                    }
                    current = System.currentTimeMillis();
                    while (task.isRunning()) {
                        if (System.currentTimeMillis() - current > period) {
                            current += period;
                            if (task.isRunning()) task.run();
                        }
                    }
                    return;
                }
            }
            cancel(task.getId());
        }).start();
        return task;
    }

    public void cancel(int id) {
        SchedulerTask task = getTask(id);
        if (task == null) return;
        task.cancel();
        tasks.remove(task);
    }

    public void cancel() {
        tasks.forEach(task -> cancel(task.getId()));
    }

    public SchedulerTask getTask(int id) {
        return tasks.stream().filter(tasks -> tasks.getId() == id).findFirst().orElse(null);
    }

    public List<SchedulerTask> getTasks() {
        return tasks;
    }
}