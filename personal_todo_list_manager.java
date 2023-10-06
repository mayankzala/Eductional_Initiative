import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

// Task class with Builder pattern for construction
class Task {
    private String description;
    private boolean completed;
    private Date dueDate;

    private Task(Builder builder) {
        this.description = builder.description;
        this.completed = false; // Tasks are initially not completed
        this.dueDate = builder.dueDate;
    }

    // Getter methods

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public Date getDueDate() {
        return dueDate;
    }

    // Builder class
    public static class Builder {
        private String description;
        private Date dueDate;

        public Builder(String description) {
            this.description = description;
        }

        public Builder dueDate(String dueDate) {
            try {
                this.dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDate);
            } catch (ParseException e) {
                e.printStackTrace(); // Handle parsing exception
            }
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }

    // Other methods for marking completion, etc.
    public void markCompleted() {
        this.completed = true;
    }

    public void markPending() {
        this.completed = false;
    }
}

// Caretaker class for Memento pattern
class TaskHistory {
    private Stack<List<Task>> history = new Stack<>();

    public void saveState(List<Task> tasks) {
        history.push(new ArrayList<>(tasks));
    }

    public List<Task> undo() {
        if (!history.isEmpty()) {
            return history.pop();
        }
        return new ArrayList<>();
    }
}

// To-Do List Manager class
class ToDoListManager {
    private List<Task> tasks = new ArrayList<>();
    private TaskHistory taskHistory = new TaskHistory();

    public void addTask(Task task) {
        tasks.add(task);
        taskHistory.saveState(tasks);
    }

    public void markCompleted(String description) {
        for (Task task : tasks) {
            if (task.getDescription().equals(description)) {
                task.markCompleted();
                taskHistory.saveState(tasks);
                break;
            }
        }
    }

    public void deleteTask(String description) {
        tasks.removeIf(task -> task.getDescription().equals(description));
        taskHistory.saveState(tasks);
    }

    public List<Task> viewTasks(String filter) {
        List<Task> filteredTasks = new ArrayList<>();
        switch (filter) {
            case "Show all":
                filteredTasks = new ArrayList<>(tasks);
                break;
            case "Show completed":
                for (Task task : tasks) {
                    if (task.isCompleted()) {
                        filteredTasks.add(task);
                    }
                }
                break;
            case "Show pending":
                for (Task task : tasks) {
                    if (!task.isCompleted()) {
                        filteredTasks.add(task);
                    }
                }
                break;
        }
        return filteredTasks;
    }

    public void undo() {
        tasks = taskHistory.undo();
    }
}

public class personal_todo_list_manager {
    public static void main(String[] args) {
        ToDoListManager toDoListManager = new ToDoListManager();

        Task task1 = new Task.Builder("Buy groceries").dueDate("2023-09-20").build();
        Task task2 = new Task.Builder("Complete Java exercise").build();

        toDoListManager.addTask(task1);
        toDoListManager.addTask(task2);

        toDoListManager.markCompleted("Buy groceries");

        List<Task> allTasks = toDoListManager.viewTasks("Show all");
        for (Task task : allTasks) {
            System.out.println(task.getDescription() +
                    " - " + (task.isCompleted() ? "Completed" : "Pending") +
                    ", Due: " + (task.getDueDate() != null ? new SimpleDateFormat("yyyy-MM-dd").format(task.getDueDate()) : "Not specified"));
        }

        System.out.println("Undoing last action...");
        toDoListManager.undo();

        List<Task> updatedTasks = toDoListManager.viewTasks("Show all");
        for (Task task : updatedTasks) {
            System.out.println(task.getDescription() +
                    " - " + (task.isCompleted() ? "Completed" : "Pending") +
                    ", Due: " + (task.getDueDate() != null ? new SimpleDateFormat("yyyy-MM-dd").format(task.getDueDate()) : "Not specified"));
        }
    }
}
