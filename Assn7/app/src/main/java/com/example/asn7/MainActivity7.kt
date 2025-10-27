package com.example.asn7

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
data class Student(val name: String, val grade: Float)
class MainViewModel : ViewModel() {
    //Q1 OBSERVED COMPOSE STATES, so changes to these variables will recompose any composable that they
    //are passed through
    //This makes sure that the UI cant change any state directly since these are private, instead
    //run the getters and setters
    private val _students = mutableStateListOf<Student>()
    val students: List<Student> get() = _students

    var newStudentName by mutableStateOf("")
        private set
    var newStudentGradeText by mutableStateOf("")
        private set
    var gpa by mutableFloatStateOf(0f)
        private set
    //Q2 This code is unidirectional because data will only be sent to he logic page, and not the other way
    //around, where you update these values with a setter and the UI will jsut read the data not recieve it
    // an added benefit of this is resource management since the UI will only recompose when
    // data is updated and doesnt handle logic on the ui area.
    fun updateNewStudentName(name: String) {
        newStudentName = name
    }

    fun updateNewStudentGrade(gradeText: String) {
        newStudentGradeText = gradeText
    }

    fun addStudent() {
        val name = newStudentName.trim()
        val grade = newStudentGradeText.toFloatOrNull() ?: return
        _students.add(Student(name, grade.coerceIn(0f, 100f)))
        newStudentName = ""
        newStudentGradeText = ""
        recalcGpa()
    }

    fun removeStudent(student: Student) {
        _students.remove(student)
        recalcGpa()
    }

    fun loadSampleData() {
        _students.clear()
        _students.addAll(
            listOf(
                Student("Alice Johnson", 95f),
                Student("Bob Smith", 87f),
                Student("Carol Davis", 92f)
            )
        )
        recalcGpa()
    }

    fun calculateGPA(): Float {
        if (_students.isEmpty()) return 0f
        return _students.map { it.grade }.average().toFloat()
    }

    private fun recalcGpa() {
        gpa = calculateGPA()
    }
    fun clearStudents() {
        _students.clear()
        gpa = 0f
    }
    /*

     How does the MainViewModel effectively manage the various pieces of data
     (e.g., student list, loading status, input fields) that need to be observed and updated by the UI?



    Explain how the interaction between the StudentGradeManager Composable and the MainViewModel
    demonstrates a unidirectional data flow. Why is this pattern beneficial for UI development?

    Describe the lifecycle of the MainViewModel in relation to MainActivity.
    Why is it important for the ViewModel to outlive configuration changes, and how does viewModel() assist with this?


    Explain the key advantages of using LazyColumn for displaying the list of students instead of a
    regular Column with a scroll modifier. When would you choose one over the other?

The obvious benefits are for large lists where you only want to display where youre looking at
to save memory rather than loading the entire list at once, I however kept expirienceing crashes using lazy column

    How does the design of this application (with MainActivity, MainViewModel, and various
     Composable functions) exemplify the principle of separation of concerns? What are the benefits of this approach?

This question was pretty much answered in q2 where it describes the flow of data from user input
to the functions that control the logic behind the MVM file, which is handling the UI
to seperate the logic and functions not only lets each file load faster, but makes
it much easier to edit and

     */
}





