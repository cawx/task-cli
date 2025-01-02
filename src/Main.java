import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        TaskActions taskActions = new TaskActions();
        Scanner sc = new Scanner(System.in);
        System.out.println("welcome to task tracker cli.");
        System.out.println("insert \"help\" for list of commands & \"quit\" to exit.");

        while(true) {
            String input = sc.nextLine().trim();
            String[] userCmd = input.split(" ", 2);
            String mainCmd = userCmd[0].toLowerCase();

            switch (mainCmd) {
                case "add":
                    if (userCmd.length < 2) {
                        System.out.println("error: task description is missing");
                        break;
                    }
                    taskActions.addTask(userCmd[1]);
                    break;
                case "update":
                    if (userCmd.length < 2) {
                        System.out.println("error: missing arguments id and/or description");
                        break;
                    }
                    String[] updateInput = userCmd[1].split(" ", 2);
                    taskActions.updateTask(Integer.valueOf(updateInput[0]), updateInput[1]);
                    break;
                case "delete":
                    taskActions.removeTask(Integer.valueOf(userCmd[1]));
                    break;
                case "list":
                    TaskStatus userStatus = userCmd.length > 1 ? getStatus(userCmd[1]) : null;
                    taskActions.listTasks(Optional.ofNullable(userStatus));
                    break;
                case "mark-todo":
                case "mark-in-progress":
                case "mark-done":
                    taskActions.markStatus(Integer.valueOf(userCmd[1]), getStatus(mainCmd));
                    break;
                case "help":
                    System.out.println("list of commands");
                    break;
                case "quit":
                    return;
                default:
                    System.out.println("invalid command. try \"help\" for a list of commands.");
            }
        }
    }

    private static TaskStatus getStatus(String cmd) {
        return switch (cmd) {
            case "mark-todo", "todo" -> TaskStatus.TODO;
            case "mark-in-progress", "in-progress" -> TaskStatus.IN_PROGRESS;
            case "mark-done", "done" -> TaskStatus.DONE;

            default -> throw new IllegalArgumentException("invalid status");
        };
    }
}