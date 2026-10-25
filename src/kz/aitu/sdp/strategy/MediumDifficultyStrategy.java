package kz.aitu.sdp.strategy;

public class MediumDifficultyStrategy implements TaskSelectionStrategy{
    private static final int TARGET_DIFFICULTY = 3;

    @Override
    public JarvisTask selectTask(JarvisTask[] tasks) {
        JarvisTask closest = tasks[0];
        for(JarvisTask task : tasks) {
           int distance = Math.abs(task.getDifficulty() - TARGET_DIFFICULTY);
           if(distance < Math.abs(closest.getDifficulty() - TARGET_DIFFICULTY)) {
               closest = task;
           }
        }
        return closest;
    }
}
