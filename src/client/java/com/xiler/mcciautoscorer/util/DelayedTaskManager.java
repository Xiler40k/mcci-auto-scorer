package com.xiler.mcciautoscorer.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DelayedTaskManager {
    private static final List<DelayedTask> tasks = new ArrayList<>();

    public static void schedule(Runnable task, int ticksDelay) {
        tasks.add(new DelayedTask(task, ticksDelay));
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Iterator<DelayedTask> iterator = tasks.iterator();
            while (iterator.hasNext()) {
                DelayedTask delayedTask = iterator.next();
                delayedTask.ticksLeft--;
                if (delayedTask.ticksLeft <= 0) {
                    delayedTask.task.run();
                    iterator.remove();
                }
            }
        });
    }

    private static class DelayedTask {
        Runnable task;
        int ticksLeft;

        DelayedTask(Runnable task, int ticksLeft) {
            this.task = task;
            this.ticksLeft = ticksLeft;
        }
    }
}
