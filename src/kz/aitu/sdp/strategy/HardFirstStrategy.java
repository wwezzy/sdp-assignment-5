package kz.aitu.sdp.strategy;

public class HardFirstStrategy implements TaskSelectionStrategy {

    @Override
    public JarvisTask selectTask (JarvisTask[] tasks) {
        JarvisTask hardest = tasks[0];
        for (JarvisTask task : tasks) {
            if(task.getDifficulty() > hardest.getDifficulty()) {
                hardest = task;
            }
        }
        return hardest;
    }
}
