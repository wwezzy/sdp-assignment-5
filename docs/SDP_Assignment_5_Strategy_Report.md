# Assignment 5 Strategy Pattern

## Jarvis Task Planner

**Student:** Allazhar Kuat  
**Group:** SE-2515  
**Course:** ShP-2216 Software Design Patterns

## 1 Introduction

I implemented a Jarvis task planner that selects the next task from an array. Each JarvisTask stores a title and a difficulty from 1 to 5. I can change the selection rule while the program is running, using the same TaskScheduler object and the same tasks.

TaskScheduler is the context, TaskSelectionStrategy is the common interface, and three concrete strategies contain the selection algorithms. StrategyDemo creates the tasks and configures the context. This follows the family of interchangeable algorithms described in Head First Design Patterns, Chapter 1.

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

## 3 Clean Code Principles

I applied six principles from Robert C. Martin's Clean Code: two from Chapter 9 Unit Tests, two from Chapter 10 Classes, and two from Chapter 11 Systems. The examples below come from the implementation and TaskSchedulerTest.

### 3 1 One Concept per Test

Chapter 9, pp. 131-132. I give each test one behavior to check. The test below checks easy-first selection; separate methods check hard-first selection, medium selection and runtime switching. Its setup, action and assertion are separated by blank lines.

```java
private static void easyFirstSelectsLowestDifficulty() {
    JarvisTask easy = new JarvisTask("Read notes", 1);
    JarvisTask hard = new JarvisTask("Complete report", 5);
    TaskScheduler scheduler = new TaskScheduler(new EasyFirstStrategy());

    JarvisTask selected = scheduler.selectNextTask(new JarvisTask[]{hard, easy});

    assertSame(easy, selected);
}
```

### 3 2 Independent and Self Validating Tests

Chapter 9, p. 132, F.I.R.S.T. I apply the Independent, Repeatable and Self-Validating rules. Each test creates its own objects and uses fixed input, so it needs no result from another test, network connection or clock. Assertions report failures automatically. The helper below checks object identity, confirming that selection returns the original task.

```java
private static void assertSame(JarvisTask expected, JarvisTask actual) {
    if (expected != actual) {
        throw new AssertionError("The selected task is not the expected task.");
    }
}
```

### 3 3 Encapsulation

Chapter 10, p. 136. I keep task data in private final fields and expose getters. The constructor rejects blank titles and difficulties outside 1-5 before assigning these fields. Strategies read valid task data without changing it.

```java
private final String title;
private final int difficulty;
```

## 3 Clean Code Continued

### 3 4 Single Responsibility

Chapter 10, pp. 138-140. Each strategy has one selection rule and one reason to change: its criterion. HardFirstStrategy only finds the maximum. TaskScheduler handles shared input checks and delegation, while StrategyDemo handles the demonstration and console output.

```java
if (task.getDifficulty() > hardest.getDifficulty()) {
    hardest = task;
}
```

### 3 5 Separate Construction from Use

Chapter 11, pp. 154-156. StrategyDemo.main creates the tasks and concrete strategies and connects them to the scheduler. TaskScheduler.selectNextTask uses the supplied strategy through its interface. It does not construct or choose an algorithm internally.

```java
TaskSelectionStrategy strategy = new EasyFirstStrategy();
TaskScheduler scheduler = new TaskScheduler(strategy);
JarvisTask selected = scheduler.selectNextTask(tasks);
```

### 3 6 Dependency Injection

Chapter 11, p. 157. I supply the initial strategy through the constructor and replace it through setStrategy. This is manual dependency injection: the client supplies the dependency, and the scheduler stores and uses it. A framework is unnecessary for this small program.

```java
scheduler.setStrategy(new HardFirstStrategy());
selected = scheduler.selectNextTask(tasks);
```

```java
return strategy.selectTask(tasks);
```

The first excerpt is the client call; the return statement is the scheduler's delegation. The context has no if/switch on concrete strategy types. Its conditions only validate inputs. A new strategy can implement the interface without changing the context.

## 4 Conclusion

I can change the rule for selecting the next task without recreating the planner or changing task data. The cost is an extra interface and three small classes, and the client still needs to choose an appropriate rule. For these three runtime modes, separating the algorithms makes their behavior easier to follow and extend.

## 5 GitHub Repository

[GitHub repository](https://github.com/wwezzy/sdp-assignment-5)
