import androidx.compose.desktop.DesktopTheme
import androidx.compose.desktop.Window
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Notifier
import java.util.*

fun main() = Window(title = "Todo List") {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Todo List - @jggomezt") })
        }
    ) {
        MaterialTheme {
            DesktopTheme {
                val tasks = TaskController.tasks

                Row(modifier = Modifier.padding(16.dp)) {
                    TaskInput(
                        TaskController.taskText.value,
                        modifier = Modifier.weight(0.5f),
                        onTextChanged = { TaskController.onChangeTaskText(it) },
                        onAddClicked = {
                            TaskController.addTask(
                                Task(
                                    UUID.randomUUID().toString(),
                                    TaskController.taskText.value,
                                    false
                                )
                            )
                            Notifier().notify("Todo List", "You just added a new task")
                            TaskController.onChangeTaskText("")
                        }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    TodoList(
                        tasks = tasks.sortedBy { it.text },
                        modifier = Modifier.weight(0.5f),
                        onDoneChecked = { idTask, done ->
                            TaskController.check(idTask, done)
                        },
                        onDelete = { idTask ->
                            TaskController.removeTask(idTask)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TodoList(
    tasks: List<Task>,
    modifier: Modifier = Modifier,
    onDoneChecked: (idTask: String, done: Boolean) -> Unit,
    onDelete: (idTask: String) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        items(tasks) { task ->
            Todo(
                task = task,
                onDoneChecked = onDoneChecked,
                onDelete = onDelete
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun Todo(
    task: Task,
    modifier: Modifier = Modifier,
    onDoneChecked: (idTask: String, done: Boolean) -> Unit,
    onDelete: (idTask: String) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(3.dp, MaterialTheme.colors.primary, RoundedCornerShape(15.dp))
            .padding(8.dp)
    ) {
        Checkbox(
            checked = task.isDone,
            modifier = Modifier.align(Alignment.CenterVertically),
            onCheckedChange = { onDoneChecked(task.id, !task.isDone) }
        )
        Spacer(modifier = Modifier.width(8.dp))

        val textTask = if (task.isDone) {
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        textDecoration = LineThrough,
                        fontSize = 17.sp
                    )
                ) {
                    append(task.text)
                }
            }
        } else {
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                ) {
                    append(task.text)
                }
            }
        }

        Text(
            text = textTask,
            modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.width(8.dp))

        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = { onDelete(task.id) }
        ) {
            Icon(
                Icons.Filled.Delete,
                "Delete Task",
                tint = MaterialTheme.colors.secondary
            )
        }
    }
}

@Composable
fun TaskInput(
    text: String,
    modifier: Modifier = Modifier,
    onTextChanged: (textTask: String) -> Unit,
    onAddClicked: () -> Unit
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().onKeyEvent {
                if (it.key == Key.Enter) {
                    onAddClicked()
                }
                true
            },
            value = text,
            onValueChange = onTextChanged,
            label = { Text(text = "Description") }
        )
        Spacer(Modifier.height(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onAddClicked
        ) {
            Text("Add Task")
        }
    }
}
