package kz.aitu.sdp.strategy;

public class JarvisTask {

    private final String title;
    private final int difficulty;

    public JarvisTask(String title, int difficulty) {

        if (1 > difficulty || difficulty > 5) {
            throw new IllegalArgumentException("Difficulty must be between 1 and 5.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Task title must not be blank.");
        }

        this.title = title;
        this.difficulty = difficulty;

    }

    public String getTitle() {
        return title;
    }

    public int getDifficulty() {
        return difficulty;
    }
}


