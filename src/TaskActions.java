import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

public class TaskActions {

    LocalDateTime currentDate = LocalDateTime.now();
    private static final String filePath = "src/TaskDb.json";
    File file = new File(filePath);
    private Map<Integer, Task> tasks;

    public TaskActions() throws IOException {
        tasks = readTasks();
    }

    private static final TypeAdapter<LocalDateTime> localDateTimeAdapter = new TypeAdapter<>() {
        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            out.value(value != null ? value.toString() : null);
        }

        @Override
        public LocalDateTime read(JsonReader in) throws IOException {
            String str = in.nextString();
            return str != null ? LocalDateTime.parse(str) : null;
        }
    };

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, localDateTimeAdapter)
            .create();


    public void addTask(String description) throws IOException {

        int newId = tasks.isEmpty() ? 1 :
                tasks.keySet().stream()
                        .mapToInt(Integer::intValue)
                        .max()
                        .getAsInt()+1;

        Task newTask = new Task(
            newId, description, currentDate, currentDate
        );

        tasks.put(newId, newTask);
        writeTasks();

        System.out.println("task added: " + description);
    }

    public void updateTask(Integer id, String description) throws IOException {
        if (!tasks.containsKey(id)) {
            System.out.println("task " + id + " not found.");
            return;
        }
        tasks.get(id).setDescription(description);
        writeTasks();
        System.out.println("task " + id + " updated: " + description);
    }

    public void removeTask(Integer id) throws IOException {
        Task removedTask = tasks.remove(id);
        if(removedTask == null) {
            System.out.println("task " + id + " not found.");
            return;
        }
        writeTasks();
        System.out.println("task removed " + id);
    }

    public void markStatus(Integer id, TaskStatus status) throws IOException {
        Task updatedTask = tasks.get(id);
        if (updatedTask == null) {
            System.out.println("task " + id + " not found.");
            return;
        }
        updatedTask.setStatus(status);
        writeTasks();
        System.out.println("task status " + id + " updated: " + status);
    }

    public void listTasks(Optional<TaskStatus> status) throws IOException {
        if(tasks.isEmpty()) {
            System.out.println("no tasks.");
            return;
        }
        if(status.isEmpty()) {
            tasks.values().forEach(task -> System.out.println(task.toString()));
        } else {
            List<Task> filteredTasks = tasks.values().stream()
                    .filter(task -> task.getStatus() == status.get())
                    .toList();

            if (filteredTasks.isEmpty()) {
                System.out.println("no tasks with " + status.get().toString().toLowerCase() + " status.");
            } else {
                filteredTasks.forEach(task -> System.out.println(task.toString()));
            }
        }
    }

    private Map<Integer,Task> readTasks() throws IOException {
        tasks = new HashMap<>();

        if(!file.exists()) {
            file.createNewFile();
        }

        try (Reader r = new FileReader(file)) {
            Type taskMapType = new TypeToken<HashMap<Integer, Task>>(){}.getType();
            tasks = gson.fromJson(r, taskMapType);
            if (tasks == null) {
                tasks = new HashMap<>();
            }
        } catch (IOException e) {
            System.out.println("something went wrong.");
        }

        return tasks;
    }
    private void writeTasks() throws IOException {
        try(FileWriter w = new FileWriter(file)) {
            gson.toJson(tasks, w);
        } catch (IOException e) {
            System.out.println("something went wrong.");
        }
    }
}
