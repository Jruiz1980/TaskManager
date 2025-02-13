import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.system.exitProcess

// Base class Task
open class Task(
    var title: String,
    var description: String,
    var dueDate: LocalDate,
    var priority: Int
)

// WorkTask class inheriting from Task
class WorkTask(
    title: String,
    description: String,
    dueDate: LocalDate,
    priority: Int,
    var projectName: String
) : Task(title, description, dueDate, priority)

// PersonalTask class inheriting from Task
class PersonalTask(
    title: String,
    description: String,
    dueDate: LocalDate,
    priority: Int,
    var personalNotes: String
) : Task(title, description, dueDate, priority)

// TaskManager interface
interface TaskManager {
    fun addTask(category: String, task: Task)
    fun editTask(taskId: Int, newTask: Task)
    fun deleteTask(taskId: Int)
    fun getStatistics(): Map<String, Int>
}

// TaskManager implementation
class SimpleTaskManager : TaskManager {
    val tasksByCategory: MutableMap<String, MutableList<Task>> = mutableMapOf("Work" to mutableListOf(), "Personal" to mutableListOf())
    private var taskCounter: Int = 0

    override fun addTask(category: String, task: Task) {
        tasksByCategory[category]?.add(task)
        taskCounter++
    }

    override fun editTask(taskId: Int, newTask: Task) {
        // Implementation to edit a task
    }

    override fun deleteTask(taskId: Int) {
        // Implementation to delete a task
    }

    override fun getStatistics(): Map<String, Int> {
        // Implementation to get statistics
        return mapOf("Total Tasks" to taskCounter, "Completed Tasks" to 0)
    }

    fun filterTasks(filterFunc: (Task) -> Boolean): Map<String, List<Task>> {
        return tasksByCategory.mapValues { (_, tasks) -> tasks.filter(filterFunc) }
    }

    fun sortTasks(keyFunc: (Task) -> Comparable<Any>) {
        tasksByCategory.values.forEach { it.sortBy { task -> keyFunc(task) } }
    }
}

// Function to read a date from the user
fun readDate(prompt: String): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    while (true) {
        println(prompt)
        try {
            return LocalDate.parse(readLine(), formatter)
        } catch (e: Exception) {
            println("Invalid date. Please use the format yyyy-MM-dd.")
        }
    }
}

// Function to read an integer from the user
fun readInt(prompt: String): Int {
    while (true) {
        println(prompt)
        try {
            return readLine()?.toInt() ?: 0
        } catch (e: Exception) {
            println("Invalid input. Please enter an integer.")
        }
    }
}

// Main function with user interaction
fun main() {
    val taskManager = SimpleTaskManager()

    while (true) {
        println("\n--- Task Manager ---")
        println("1. Add Task")
        println("2. Filter Tasks by Priority")
        println("3. Sort Tasks by Due Date")
        println("4. Exit")
        print("Select an option: ")

        when (readLine()) {
            "1" -> {
                println("Enter the task category (Work/Personal): ")
                val category = readLine() ?: ""
                println("Enter the task title: ")
                val title = readLine() ?: ""
                println("Enter the task description: ")
                val description = readLine() ?: ""
                val dueDate = readDate("Enter the due date (yyyy-MM-dd): ")
                val priority = readInt("Enter the task priority (integer): ")

                if (category == "Work") {
                    println("Enter the project name: ")
                    val projectName = readLine() ?: ""
                    val workTask = WorkTask(title, description, dueDate, priority, projectName)
                    taskManager.addTask(category, workTask)
                } else if (category == "Personal") {
                    println("Enter personal notes: ")
                    val personalNotes = readLine() ?: ""
                    val personalTask = PersonalTask(title, description, dueDate, priority, personalNotes)
                    taskManager.addTask(category, personalTask)
                } else {
                    println("Invalid category. Use 'Work' or 'Personal'.")
                }
            }
            "2" -> {
                val priority = readInt("Enter the priority to filter: ")
                val filteredTasks = taskManager.filterTasks { it.priority == priority }
                filteredTasks.forEach { (category, tasks) ->
                    println("\nCategory: $category")
                    tasks.forEach { task ->
                        println("Title: ${task.title}, Description: ${task.description}, Due Date: ${task.dueDate}, Priority: ${task.priority}")
                    }
                }
            }
            "3" -> {
                taskManager.sortTasks { task -> task.dueDate as Comparable<Any> }
                taskManager.tasksByCategory.forEach { (category, tasks) ->
                    println("\nCategory: $category")
                    tasks.forEach { task ->
                        println("Title: ${task.title}, Description: ${task.description}, Due Date: ${task.dueDate}, Priority: ${task.priority}")
                    }
                }
            }
            "4" -> exitProcess(0)
            else -> println("Invalid option. Try again.")
        }
    }
}