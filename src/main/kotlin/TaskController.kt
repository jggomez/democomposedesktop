import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

data class Task(val id: String, val text: String, var isDone: Boolean)

object TaskController {

    var tasks = mutableStateListOf<Task>()
        private set

    var taskText = mutableStateOf("")
        private set

    fun onChangeTaskText(text: String) {
        taskText.value = text
    }

    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun removeTask(idTask: String) {
        tasks.firstOrNull { it.id == idTask }?.let {
            tasks.remove(it)
        }
    }

    fun check(idTask: String, check: Boolean) {
        tasks.firstOrNull { it.id == idTask }?.let {
            val updateTask = it.copy(isDone = check)
            tasks.remove(it)
            tasks.add(updateTask)
        }
    }
}
