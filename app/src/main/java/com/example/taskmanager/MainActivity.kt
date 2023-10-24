package com.example.taskmanager

import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import com.example.taskmanager.ui.theme.TaskManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskManagerTheme {
                App()
            }
        }
    }
}


data class Task(val id: Int,val info: String, var complete: Boolean = false)

val list = List(20) {Task(it + 1, "Tarea ${it + 1}", false)}.toMutableStateList()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskManager(tasksList : List<Task>, onComplete : (Task) -> Unit, onDelete : (Task) -> Unit, onAdd : (String) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var newTaskText by remember { mutableStateOf("") }
        TextField(
            value = newTaskText,
            onValueChange = { newTaskText = it },
            placeholder = { Text("Introducir nueva tarea") }
        )
        Button(
            onClick = {
                if (newTaskText.isNotEmpty()) {
                    onAdd(newTaskText)
                    newTaskText = ""
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Añadir Tarea")
        }
    }

    LazyColumn {
        items(tasksList) {
            task ->
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Checkbox(checked = task.complete, onCheckedChange = {isChecked -> task.complete = isChecked
                    onComplete
                })

                Text(text = task.info,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 14.dp),
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                IconButton(onClick = {onDelete(task)}, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Deleted", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}


@Composable
fun App() {
    val tasksList by remember { mutableStateOf(list) }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
        ) {

            TaskManager(
                tasksList = tasksList,
                onComplete = { task ->
                    task.complete = !task.complete
                    },
                onDelete = { task ->
                    tasksList.remove(task)
                },
                onAdd = { newTaskText ->
                    val newTask = Task(tasksList.size + 1, newTaskText, false)
                    tasksList.add(newTask)
                }
            )
        }
    }
}