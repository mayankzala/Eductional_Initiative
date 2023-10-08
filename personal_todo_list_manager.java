import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.time.LocalDate;
import java.util.ArrayList;
// Task class- description of task , due date of task, completed status

class Task {
    // Not accessible to user(view only)
    private String task_description;
    private LocalDate dueDate;
    private boolean is_completed;

    // constructer to set the parameters of Task
    Task(String description, LocalDate date) {
        this.task_description = description;
        this.dueDate = date;
        this.is_completed = false; // initialy task is incompleted;
    }

    // methods to get the task data
    public String get_Task_Description() {
        return task_description;
    }

    public LocalDate get_dueDate() {
        return dueDate;
    }

    public boolean is_completed() {
        return is_completed;
    }

    // method to make task's status completed
    public void setCompleted() {
        this.is_completed = true;
    }

    // update the description and date;
    public void update_Description(String update_desc) {
        this.task_description = update_desc;
    }

    public void update_dueDate(LocalDate d) {
        this.dueDate = d;
    }

}

// Memento Design pattern to save history
class undo_redo {
    private Stack<List<Task>> undo_stack = new Stack<>();
    private Stack<List<Task>> redo_stack = new Stack<>();

    public void save_Current(List<Task> tasks) {
        undo_stack.push(tasks);
    }

    // undo operation
    public List<Task> undo() {
        List<Task> temp = undo_stack.pop();
        if (!undo_stack.isEmpty()) {
            redo_stack.push(temp);
            return undo_stack.isEmpty() ? new ArrayList<>() : undo_stack.peek();
        }
        return new ArrayList<>();
    }

    // redo operation
    public List<Task> redo() {
        List<Task> temp = redo_stack.pop();
        if (!redo_stack.isEmpty()) {
            undo_stack.push(temp);
            return undo_stack.isEmpty() ? new ArrayList<>() : undo_stack.peek();
        }
        return undo_stack.peek();
    }

}

class todoList_Manager {
    // Arraylist used because of dynamic memory allocation for store any no. of task
    List<Task> task_Storage = new ArrayList<>();// stores the Task object;

    // object of undo_redo
    undo_redo u_r = new undo_redo();

    public void add_Task(Task t) {
        task_Storage.add(t);

        List<Task> tasksCopy = new ArrayList<>(task_Storage);
        u_r.save_Current(tasksCopy);
    }

    // considering decription as id for identifying the task
    public void mark_Complete(String decs) {
        for (Task t : task_Storage) {
            if (t.get_Task_Description().equals(decs)) {
                t.setCompleted();
                break;
            }
        }
        List<Task> tasksCopy = new ArrayList<>();
        tasksCopy.addAll(task_Storage);
        u_r.save_Current(tasksCopy);
    }

    public void delete_Task(String decs) {
        for (Task t : task_Storage) {
            if (t.get_Task_Description().equals(decs)) {
                task_Storage.remove(t);

                break;
            }
        }
        List<Task> tasksCopy = new ArrayList<>();
        tasksCopy.addAll(task_Storage);
        u_r.save_Current(tasksCopy);
    }

    public void print_TaskList(int choice) {

        System.out.println("Due LocalDate  \t  Status\tDescription");
        System.out.println("--------------------------------------------------------");
        for (Task t : task_Storage) {
            if (choice == 2) {
                if (t.is_completed()) {
                    System.out.print(t.get_dueDate() + "        ");
                    System.out.print("Completed    ");
                    System.out.print(t.get_Task_Description());
                    System.out.println();
                } else {
                    continue;
                }
            } else if (choice == 3) {
                if (!t.is_completed()) {
                    System.out.print(t.get_dueDate() + "        ");
                    System.out.print("Pending      ");
                    System.out.print(t.get_Task_Description());
                    System.out.println();
                } else {
                    continue;
                }
            } else {
                System.out.print(t.get_dueDate() + "        ");
                System.out.print(t.is_completed() ? "Completed    " : "Pending      ");
                System.out.print(t.get_Task_Description());
                System.out.println();
            }
        }
        System.out.println("\n\n");
    }

    public void undo() {

        List<Task> undoTasks = u_r.undo();
        // this.task_Storage=u_r.undo();

        if (undoTasks != null) {
            this.task_Storage = undoTasks;
        }
    }

    public void redo() {
        List<Task> redoTasks = u_r.redo();
        if (redoTasks != null) {
            this.task_Storage = redoTasks;
        }
    }

}

public class personal_todo_list_manager {
    public static void main(String[] args) {

        todoList_Manager manager = new todoList_Manager();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Available Operation:");
            System.out.println("press 1:Add Task");
            System.out.println("press 2.Remove Task(Require task description)");
            System.out.println("press 3:Mark Complete.(Required task decsription)");
            System.out.println("press 4:Display Task");// all task, completed task,// pending task
            System.out.println("press 5:undo");
            System.out.println("press 6:redo");
            System.out.println("press 7:exit");
            System.out.println("----------------");
            System.out.print("Choose the operation:");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    // Scanner scanner = new Scanner(System.in);

                    // Taking user input for task description
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Enter task description: ");
                    String description = scanner.nextLine();
                    // Taking user input for due date (assuming date format yyyy-MM-dd)
                    System.out.print("Enter due date (eg 2023-09-09): ");
                    String dueDateString = scanner.nextLine();

                    // Parsing the due date string into a LocalDate object
                    LocalDate dueDate = LocalDate.parse(dueDateString);
                    Task t = new Task(description, dueDate);
                    manager.add_Task(t);
                    System.out.println("Task Added Successfully!");
                    break;
                case 2:
                    Scanner sc2 = new Scanner(System.in);
                    System.out.print("Task Description(of one you want to remove ):");
                    String desc2 = sc2.nextLine();
                    System.out.println(desc2);
                    manager.delete_Task(desc2);
                     System.out.println("Task Deleted Successfully!");
                    break;
                case 3:
                    Scanner sc3 = new Scanner(System.in);
                    System.out.print("Task Description(of one you want to update ):");
                    String desc3 = sc3.nextLine();
                    System.out.println(desc3);
                    manager.mark_Complete(desc3);
                    System.out.println("Task marked completed Successfully!");
                    break;
                case 4:
                    Scanner sc4 = new Scanner(System.in);
                    System.out.print("Press 1: Show All\nPress 2: Show Completed\nPress 3:Show Pending\nChoose:");
                    int show = sc4.nextInt();
                    manager.print_TaskList(show);
                    break;
                case 5:
                    manager.undo();
                    System.out.println("Undo Successfully!");
                    break;
                    case 6:
                    manager.redo();
                    System.out.println("Redo Successfully!");
                    break;
                case 7:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Enter valid choice");
            }
            System.out.println("_____________________________________________");

        }
        
    }
}