package com.example.assn_6



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LoginApp()
                }
            }
        }
    }
}

@Composable                                                                            //Question 3
fun LoginApp() {
    val navController = rememberNavController()                                        //Question 1

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen { name ->                                                      //Question 2
                navController.navigate("welcome/$name") {
                    popUpTo("login") { inclusive = false }
                }
            }
        }
        composable(
            route = "welcome/{userName}",
            arguments = listOf(navArgument("userName") { type = NavType.StringType })
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName").orEmpty()
            WelcomeScreen(
                userName = userName,
                onViewProfile = {
                    navController.navigate("profile/$userName")
                },
                onLogout = {
                    navController.popBackStack(route = "login", inclusive = false)
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "profile/{userName}",
            arguments = listOf(navArgument("userName") { type = NavType.StringType })
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName").orEmpty()
            ProfileScreen(
                userName = userName,
                onBackToWelcome = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf(false) }
    var emailFormatError by remember { mutableStateOf(false) }
    var emailProvidedError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    var passwordVisible by remember { mutableStateOf(false) }

    val validEmail = "student@wccnet.edu"
    val validPassword = "password123"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Student Login",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))

        Card(elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; nameError = false },
                    label = { Text("Full Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    isError = nameError,
                    supportingText = {
                        if (nameError) Text("Please enter your name")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailProvidedError = false
                        emailFormatError = email.isNotEmpty() && !isValidEmail(email)
                    },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    isError = emailFormatError || emailProvidedError,
                    supportingText = {
                        when {                                                         //Question 4
                            emailFormatError -> Text("Please enter a valid email")
                            emailProvidedError -> Text("Please enter the correct email")
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; passwordError = false },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        TextButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(if (passwordVisible) "Hide" else "Show")
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = passwordError,
                    supportingText = { if (passwordError) Text("Please enter the correct password") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        nameError = name.isBlank()
                        emailFormatError = email.isEmpty() || !isValidEmail(email)
                        emailProvidedError = false                                  //question 4
                        passwordError = false

                        if (!nameError && !emailFormatError) {
                            if (email != validEmail) {
                                emailProvidedError = true
                            } else if (password != validPassword) {
                                passwordError = true
                            } else {
                                onLoginSuccess(name.trim())
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }
                Text(
                    text = "Demo: $validEmail / $validPassword",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )

            }
        }
    }
}

@Composable
fun WelcomeScreen(
    userName: String,
    onViewProfile: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome!",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Hello, $userName!",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onViewProfile, modifier = Modifier.fillMaxWidth()) {
            Text("View Profile")
        }
        Spacer(Modifier.height(12.dp))
        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text("Logout")
        }
    }
}

@Composable
fun ProfileScreen(
    userName: String,
    onBackToWelcome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "User Profile",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))

        Card(elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProfileRow("Name", userName)
                ProfileRow("Email", "student@wccnet.edu")
                ProfileRow("Student ID", "2024001")
                ProfileRow("Major", "Computer Science")
                ProfileRow("Year", "Freshman")
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onBackToWelcome, modifier = Modifier.fillMaxWidth()) {
            Text("Back to Welcome")
        }
    }
}

@Composable
fun ProfileRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}

fun isValidEmail(email: String): Boolean {                                             //Question 4
    val pattern = Regex("""^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$""")
    return pattern.matches(email)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme { LoginScreen(onLoginSuccess = {}) }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    MaterialTheme { WelcomeScreen("John Doe", onViewProfile = {}, onLogout = {}) }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme { ProfileScreen("John Doe", onBackToWelcome = {}) }
    /*
    -QNA
Q.The LoginApp composable uses NavHost and rememberNavController().
Explain the primary purpose of NavHost and NavController in defining the overall navigation structure
of an Android Compose application.

A. the structure set up here has navcontroller managing the pages and stack so changing the screen
will call nav controller
whereas nav host defines and manages the screens, maps the route that
navcontroler will change between





Q.The LoginApp defines routes like "welcome/{userName}" and "profile/{userName}".
Explain the mechanism used to pass the userName data from the LoginScreen to WelcomeScreen
and ProfileScreen. Specifically, reference how arguments are defined in the route pattern and
retrieved in the target composable as discussed in the "Passing Arguments" lesson.

A.you can see where I defined the route for the variable to be in "welcome/{userName}" and "profile/{userName}"
setting the placeholder
navArgument("userName") { type = NavType.StringType }
, and injecting that string or object into the next page
navController.navigate("welcome/$name")
then grabbing the variable from the stack where it was injected
backStackEntry.arguments?.getString("userName")
allows the new screen to call the variable





Q.The solution you are to create breaks down the UI into
LoginScreen, WelcomeScreen, ProfileScreen.
Discuss the advantages of organizing UI into distinct composable functions and potentially
separate files for improved maintainability and readability,
even if this specific project keeps them in one file.

A. the obvious answer is to seperate concerns, and manage each page seperately, allowing easier debugging
and readability
when recomposing as well it will not have to scan the whole file for what you are calling
this also helps with scalability, where you dont have to sift through code to insert a new page, you can
call it from its own file.
this also adds the ability to seperate navagation, and break down the concerns even further
which makes defining destionations easier.






Q. For the email field either the email is of the wrong format,
the incorrect email, or is fine.  Show the code where you programed the
logic so it knows what error message to display based upon what is wrong.

A. the email input has two filters as opposed to the one for name and password as we're checking it
against the regex pattern to ensure its formatted correctly.
it will either bounce because it is not the email defined in the true email, or its pattern doesnt match the regex





Q.Although Top App Bar and Bottom Navigation Bar are discussed in navigation_ui.html,
this solution primarily uses buttons for navigation within the content area.
Discuss a scenario in a larger application where you might choose to implement a
Bottom Navigation Bar (as described in navigation_ui.html) as the primary navigation
instead of simple buttons, and what benefits it would offer.

A.It would allow designers to consolodate different things a user can acess or do,
for example if you wanted to have an app with more than one feature, a user profile page,
and a freinds page, and a community page, for example, having to travel from one to the next
with buttons inside pages makes navatation slow and annoying, where a universal area picker will
allow users to more quickly navigate to the feature they want to use, rather than go fro profile
to friends to community. removing a restrictive expirience where users cannot choose the order
in which they acess pages.

     */
}
