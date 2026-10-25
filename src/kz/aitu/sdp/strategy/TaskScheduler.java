package kz.aitu.sdp.strategy;

public class TaskScheduler {
    private TaskSelectionStrategy strategy;

    public TaskScheduler(TaskSelectionStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Task selection strategy must not be null.");
        }
        this.strategy = strategy;
    }
    public void setStrategy(TaskSelectionStrategy strategy) {
        if(strategy == null) {
            throw new IllegalArgumentException("Strategy must not be null.");
        }
        this.strategy = strategy;
    }
    public JarvisTask selectNextTask(JarvisTask[] tasks) {
        if (tasks == null || tasks.length == 0) {
            throw new IllegalArgumentException("Task array must not be null or empty.");
        }
        for (JarvisTask task : tasks) {
            if (task == null) {
                throw new IllegalArgumentException("Task array must not contain null elements.");
            }
        }
        return strategy.selectTask(tasks);
    }
}
