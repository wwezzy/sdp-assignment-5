package kz.aitu.sdp.strategy;

public class StrategyDemo {
    public static void main(String[] args) {

        JarvisTask easyTask = new JarvisTask("Read notes", 1);
        JarvisTask mediumTask = new JarvisTask("Wash dishes", 3);
        JarvisTask hardTask = new JarvisTask("Complete all SDP assignments", 5);

        JarvisTask[] tasks = {easyTask, mediumTask, hardTask};

        TaskSelectionStrategy strategy = new EasyFirstStrategy();
        TaskScheduler scheduler = new TaskScheduler(strategy);
        JarvisTask selected = scheduler.selectNextTask(tasks);
        System.out.println(selected.getTitle());
        System.out.println(selected.getDifficulty());

    }
}
