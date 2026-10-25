# Assignment 5 Strategy Pattern

## Jarvis Task Planner

**Student:** Allazhar Kuat  
**Group:** SE-2515  
**Course:** ShP-2216 Software Design Patterns

## 1 Introduction

I implemented a Jarvis task planner that selects the next task from an array. Each JarvisTask stores a title and a difficulty from 1 to 5. I can change the selection rule while the program is running, using the same TaskScheduler object and the same tasks.

TaskScheduler is the context, TaskSelectionStrategy is the common interface, and three concrete strategies contain the selection algorithms. StrategyDemo creates the tasks and configures the context. This follows the family of interchangeable algorithms described in Head First Design Patterns, Chapter 1 [2].

### 1 1 Selection rules

| Strategy | Rule |
| --- | --- |
| EasyFirstStrategy | Select the smallest difficulty. |
| HardFirstStrategy | Select the largest difficulty. |
| MediumDifficultyStrategy | Select the smallest absolute distance from difficulty 3. |

Medium difficulty means the middle of the fixed 1-5 scale, not the average of the input array. Equal candidates keep their original order: each algorithm replaces its candidate only when it finds a strictly better match. The selected task is an existing object from the array.

### 1 2 Runtime demonstration

In StrategyDemo, I first select with EasyFirstStrategy, then replace it with HardFirstStrategy and MediumDifficultyStrategy. After each replacement, I call selectNextTask again.

```java
scheduler.setStrategy(new HardFirstStrategy());
selected = scheduler.selectNextTask(tasks);
```

| Mode | Selected task | Difficulty |
| --- | --- | --- |
| Easy first | Read notes | 1 |
| Hard first | Complete all SDP assignments | 5 |
| Medium difficulty | Wash dishes | 3 |

The project compiles with Java 17. Selection preserves the task values and array order.

## 2 UML Class Diagram

![Strategy class diagram](https://raw.githubusercontent.com/wwezzy/sdp-assignment-5/master/docs/strategy-uml.png)

Figure 1. The client, context, strategy interface, concrete strategies and task model.

The solid arrow from TaskScheduler shows its stored strategy reference. Dashed arrows with hollow triangles represent interface implementation; the other dashed arrows represent dependencies. The context holds one current strategy. The readOnly task fields correspond to final fields in Java.

## 3 Clean Code Chapter 10

I applied the following class design principles from Clean Code, Chapter 10 [1].

### 3 1 Encapsulation

I keep task data in private final fields and expose getters. The constructor rejects blank titles and difficulties outside 1-5 before assigning these fields. A selection strategy reads task data without changing it. This follows the chapter's guidance to keep internal data private [1, p. 136].

```java
private final String title;
private final int difficulty;
```

### 3 2 Single Responsibility

Each strategy has one selection rule and one reason to change: its criterion. EasyFirstStrategy finds the minimum; HardFirstStrategy finds the maximum. TaskScheduler handles shared input checks and delegation, while StrategyDemo handles the demonstration and console output [1, pp. 138-140].

```java
if (task.getDifficulty() > hardest.getDifficulty()) {
    hardest = task;
}
```

### 3 3 Cohesion

The methods in TaskScheduler work around the same strategy reference. The constructor establishes it, setStrategy replaces it, and selectNextTask uses it. I keep these related operations together, as described in the chapter's discussion of cohesion [1, p. 140].

```java
public void setStrategy(TaskSelectionStrategy strategy) {
    if (strategy == null) {
        throw new IllegalArgumentException(
                "Strategy must not be null.");
    }
    this.strategy = strategy;
}
```

## 3 Clean Code Continued

### 3 4 Organizing for Change

I put the variable selection algorithms behind one interface. A new rule can implement this contract without changing TaskScheduler or the existing strategies. The client would create and configure the new strategy. This applies the Open/Closed Principle discussed under Organizing for Change [1, pp. 147-149].

```java
public interface TaskSelectionStrategy {
    JarvisTask selectTask(JarvisTask[] tasks);
}
```

### 3 5 Isolating from Change

TaskScheduler depends on TaskSelectionStrategy rather than a particular algorithm class. I inject the initial strategy through the constructor and can replace it through the setter. The execution call remains the same for all three implementations, following the Dependency Inversion Principle [1, pp. 149-150].

```java
return strategy.selectTask(tasks);
```

The context contains no if/switch that chooses a concrete strategy type. Its conditions validate the input array: it must exist, contain at least one task and contain no null elements. The strategies receive this validated input.

## 4 Conclusion

I can change the rule for selecting the next task without recreating the planner or changing task data. The cost is an extra interface and three small classes, and the client still needs to choose an appropriate rule. For these three runtime modes, separating the algorithms makes their behavior easier to follow and extend.

## 5 GitHub Repository

[GitHub repository](https://github.com/wwezzy/sdp-assignment-5)

### References

[1] Robert C. Martin. Clean Code. Chapter 10: Classes, pp. 135-151.

[2] Head First Design Patterns. Chapter 1, pp. 22-24.

[3] Lecture 7 and Assignment 5 Strategy Pattern Instructions. ShP-2216 Software Design Patterns, Astana IT University.
