package kz.aitu.sdp.strategy;

public class EasyFirstStrategy implements TaskSelectionStrategy {

    @Override
    public JarvisTask selectTask(JarvisTask[] tasks) {
        JarvisTask easiest = tasks[0];
        for (JarvisTask task : tasks) {
            if(task.getDifficulty() < easiest.getDifficulty()) {
                easiest = task;
            }
        }
        return easiest;
    }
}



