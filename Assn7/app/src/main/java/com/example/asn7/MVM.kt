package com.example.asn7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                StudentGradeManager(vm)
            }
        }
    }
}
//Q3 I instatiate the view model here and  run it by the viewModels()
//this ensures the seperation of concerns and preserve the data in the viewModels
//is preserved through any configuration change or recomposition as the data is held in the ma7 file
//this speeds up ui processing and consolodates recomposition to the data that has actually changed.
@Composable
fun StudentGradeManager(vm: MainViewModel) {
    val scroll = rememberScrollState()
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(2000)
            vm.loadSampleData()
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Student Grade Manager", style = MaterialTheme.typography.headlineSmall)
        GPADisplay(gpa = vm.gpa, studentCount = vm.students.size)
        AddStudentForm(
            name = vm.newStudentName,
            gradeText = vm.newStudentGradeText,
            onNameChange = vm::updateNewStudentName,
            onGradeChange = vm::updateNewStudentGrade,
            onAdd = vm::addStudent
        )
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { isLoading = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Load Sample Data")
                }
            }
        }
        StudentList(students = vm.students, onRemove = vm::removeStudent)
    }
}

@Composable
fun GPADisplay(gpa: Float, studentCount: Int = 0) {
    Surface(
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Class GPA",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "%.2f".format(Locale.US, gpa),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Students: $studentCount",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun AddStudentForm(
    name: String,
    gradeText: String,
    onNameChange: (String) -> Unit,
    onGradeChange: (String) -> Unit,
    onAdd: () -> Unit
) {
    Surface(
        tonalElevation = 1.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Add New Student", fontWeight = FontWeight.SemiBold)
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Student Name") },
                singleLine = true
            )
            OutlinedTextField(
                value = gradeText,
                onValueChange = onGradeChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Grade (0–100)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            val enabled = name.isNotBlank() && gradeText.toFloatOrNull() != null
            Button(onClick = onAdd, enabled = enabled, modifier = Modifier.fillMaxWidth()) {
                Text("Add Student")
            }
        }
    }
}

@Composable
fun StudentList(students: List<Student>, onRemove: (Student) -> Unit) {
    Surface(
        tonalElevation = 1.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Students (${students.size})", fontWeight = FontWeight.SemiBold)
            if (students.isEmpty()) {
                Text("No students added yet")
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    students.forEach { StudentRow(it, onRemove) }
                }
            }
        }
    }
}

@Composable
fun StudentRow(student: Student, onRemove: (Student) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(student.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                text = "Grade: ${"%.2f".format(Locale.US, student.grade)}",
                style = MaterialTheme.typography.bodySmall
            )
        }
        IconButton(onClick = { onRemove(student) }) {
            Icon(Icons.Filled.Delete, contentDescription = "Remove student")
        }
    }
}



@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "Full App Preview")
@Composable
fun PreviewFullApp() {
    val vm = MainViewModel().apply { loadSampleData() }
    MaterialTheme {
        StudentGradeManager(vm)
    }
}
