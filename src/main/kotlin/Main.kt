import java.time.LocalDate

// Clase base Task
open class Task(
    var title: String,
    var description: String,
    var dueDate: LocalDate,
    var priority: Int
)

// Clase WorkTask que hereda de Task
class WorkTask(
    title: String,
    description: String,
    dueDate: LocalDate,
    priority: Int,
    var projectName: String
) : Task(title, description, dueDate, priority)

// Clase PersonalTask que hereda de Task
class PersonalTask(
    title: String,
    description: String,
    dueDate: LocalDate,
    priority: Int,
    var personalNotes: String
) : Task(title, description, dueDate, priority)

// Interfaz TaskManager
interface TaskManager {
    fun addTask(category: String, task: Task)
    fun editTask(taskId: Int, newTask: Task)
    fun deleteTask(taskId: Int)
    fun getStatistics(): Map<String, Int>
}

// Implementación de TaskManager
class SimpleTaskManager : TaskManager {
    private val tasksByCategory: MutableMap<String, MutableList<Task>> = mutableMapOf("Work" to mutableListOf(), "Personal" to mutableListOf())
    private var taskCounter: Int = 0

    override fun addTask(category: String, task: Task) {
        tasksByCategory[category]?.add(task)
        taskCounter++
    }

    override fun editTask(taskId: Int, newTask: Task) {
        // Implementación para editar una tarea
    }

    override fun deleteTask(taskId: Int) {
        // Implementación para eliminar una tarea
    }

    override fun getStatistics(): Map<String, Int> {
        // Implementación para obtener estadísticas
        return mapOf("Total Tasks" to taskCounter, "Completed Tasks" to 0)
    }

    fun filterTasks(filterFunc: (Task) -> Boolean): Map<String, List<Task>> {
        return tasksByCategory.mapValues { (_, tasks) -> tasks.filter(filterFunc) }
    }

    // Corregido: Funcion sortTasks con tipos explícitos
    fun sortTasks(keyFunc: (Task) -> Comparable<Any>) {
        tasksByCategory.values.forEach { it.sortBy { task -> keyFunc(task) } }
    }
}

// Ejemplo de uso
fun main() {
    val taskManager = SimpleTaskManager()
    val workTask = WorkTask("Prepare report", "Prepare the quarterly report", LocalDate.of(2025, 2, 28), 1, "Q1 Reports")
    val personalTask = PersonalTask("Buy groceries", "Buy groceries for the week", LocalDate.of(2025, 2, 20), 2, "Need to buy fresh produce")

    taskManager.addTask("Work", workTask)
    taskManager.addTask("Personal", personalTask)

    // Filtrar tareas por prioridad
    val highPriorityTasks = taskManager.filterTasks { it.priority == 1 }

    // Ordenar tareas por fecha de vencimiento
    taskManager.sortTasks { task -> task.dueDate as Comparable<Any> }
}
