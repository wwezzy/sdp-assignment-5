package kz.aitu.sdp.strategy;

public class TaskSchedulerTest {
    public static void main(String[] args) {
        easyFirstSelectsLowestDifficulty();
        hardFirstSelectsHighestDifficulty();
        mediumSelectsClosestAvailableDifficulty();
        mediumKeepsFirstTaskWhenDistancesAreEqual();
        schedulerUsesReplacementStrategy();
        schedulerRejectsEmptyArray();
        rejectedStrategyLeavesCurrentStrategyUsable();
        taskRejectsDifficultyAboveFive();
        System.out.println("All 8 tests passed.");
    }

    private static void easyFirstSelectsLowestDifficulty() {
        JarvisTask easy = new JarvisTask("Read notes", 1);
        JarvisTask hard = new JarvisTask("Complete report", 5);
        TaskScheduler scheduler = new TaskScheduler(new EasyFirstStrategy());

        JarvisTask selected = scheduler.selectNextTask(new JarvisTask[]{hard, easy});

        assertSame(easy, selected);
    }

    private static void hardFirstSelectsHighestDifficulty() {
        JarvisTask easy = new JarvisTask("Read notes", 1);
        JarvisTask hard = new JarvisTask("Complete report", 5);
        TaskScheduler scheduler = new TaskScheduler(new HardFirstStrategy());

        JarvisTask selected = scheduler.selectNextTask(new JarvisTask[]{easy, hard});

        assertSame(hard, selected);
    }

    private static void mediumSelectsClosestAvailableDifficulty() {
        JarvisTask easy = new JarvisTask("Read notes", 1);
        JarvisTask nearMiddle = new JarvisTask("Review examples", 2);
        JarvisTask hard = new JarvisTask("Complete report", 5);
        TaskScheduler scheduler = new TaskScheduler(new MediumDifficultyStrategy());

        JarvisTask selected = scheduler.selectNextTask(
                new JarvisTask[]{hard, easy, nearMiddle});

        assertSame(nearMiddle, selected);
    }

    private static void mediumKeepsFirstTaskWhenDistancesAreEqual() {
        JarvisTask first = new JarvisTask("Prepare slides", 4);
        JarvisTask second = new JarvisTask("Review examples", 2);
        TaskScheduler scheduler = new TaskScheduler(new MediumDifficultyStrategy());

        JarvisTask selected = scheduler.selectNextTask(new JarvisTask[]{first, second});

        assertSame(first, selected);
    }

    private static void schedulerUsesReplacementStrategy() {
        JarvisTask easy = new JarvisTask("Read notes", 1);
        JarvisTask hard = new JarvisTask("Complete report", 5);
        TaskScheduler scheduler = new TaskScheduler(new EasyFirstStrategy());

        scheduler.setStrategy(new HardFirstStrategy());
        JarvisTask selected = scheduler.selectNextTask(new JarvisTask[]{easy, hard});

        assertSame(hard, selected);
    }

    private static void schedulerRejectsEmptyArray() {
        TaskScheduler scheduler = new TaskScheduler(new EasyFirstStrategy());

        assertIllegalArgument(() -> scheduler.selectNextTask(new JarvisTask[0]));
    }

    private static void rejectedStrategyLeavesCurrentStrategyUsable() {
        JarvisTask easy = new JarvisTask("Read notes", 1);
        JarvisTask hard = new JarvisTask("Complete report", 5);
        TaskScheduler scheduler = new TaskScheduler(new EasyFirstStrategy());

        assertIllegalArgument(() -> scheduler.setStrategy(null));
        JarvisTask selected = scheduler.selectNextTask(new JarvisTask[]{hard, easy});

        assertSame(easy, selected);
    }

    private static void taskRejectsDifficultyAboveFive() {
        assertIllegalArgument(() -> new JarvisTask("Invalid task", 6));
    }

    private static void assertSame(JarvisTask expected, JarvisTask actual) {
        if (expected != actual) {
            throw new AssertionError("The selected task is not the expected task.");
        }
    }

    private static void assertIllegalArgument(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError("Expected IllegalArgumentException.");
    }
}
