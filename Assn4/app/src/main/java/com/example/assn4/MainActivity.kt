package com.example.assn4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ContactValidatorApp()
            }
        }
    }
}

@Composable
fun ContactValidatorApp() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Contact Information",
                fontSize = 30.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            ContactForm()
        }
    }
}

@Composable
fun ContactForm() {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var zipCode by rememberSaveable { mutableStateOf("") }


    val isNameValid = name.trim().length >= 2
    val isEmailValid = validateEmail(email)
    val isPhoneValid = validatePhone(phone)
    val isZipValid = validateZipCode(zipCode)

    var submittedInfo by rememberSaveable { mutableStateOf<String?>(null) }

    val isFormValid =
        isNameValid && isEmailValid && isPhoneValid && isZipValid &&
                name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && zipCode.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        NameField(
            name = name,
            isNameValid = isNameValid,
            onValueChange = { name = it }
        )

        EmailField(
            email = email,
            isEmailValid = isEmailValid,
            onValueChange = { email = it }
        )

        PhoneField(
            phone = phone,
            isPhoneValid = isPhoneValid,
            onValueChange = { phone = it }
        )

        ZipCodeField(
            zipCode = zipCode,
            isZipValid = isZipValid,
            onValueChange = { zipCode = it }
        )

        Button(
            onClick = {
                if (isFormValid) {
                    submittedInfo =
                        "Name: ${name.trim()}\nEmail: ${email.trim()}\nPhone: ${phone.trim()}\nZIP: ${zipCode.trim()}"
                }
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }

        submittedInfo?.let { it ->
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = it,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun NameField(
    name: String,
    isNameValid: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = name,
        onValueChange = onValueChange,
        label = { Text("Full Name") },
        placeholder = { Text("John Smith") },
        singleLine = true,
        isError = name.isNotBlank() && !isNameValid,
        supportingText = {
            if (name.isNotBlank() && !isNameValid) {
                Text("Name must be at least 2 characters.")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun EmailField(
    email: String,
    isEmailValid: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = email,
        onValueChange = onValueChange,
        label = { Text("Email") },
        placeholder = { Text("user@example.com") },
        singleLine = true,
        isError = email.isNotBlank() && !isEmailValid,
        supportingText = {
            if (email.isNotBlank() && !isEmailValid) {
                Text("Enter a valid email (e.g. name@domain.com).")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PhoneField(
    phone: String,
    isPhoneValid: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = phone,
        onValueChange = { input ->
            val filtered = input.filter { it.isDigit() || it == '-' || it == '/' }.take(12)
            onValueChange(filtered)
        },
        label = { Text("Phone") },
        placeholder = { Text("123-456-7890") },
        singleLine = true,
        isError = phone.isNotBlank() && !isPhoneValid,
        supportingText = {
            if (phone.isNotBlank() && !isPhoneValid) {
                Text("Format: 123-456-7890")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ZipCodeField(
    zipCode: String,
    isZipValid: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = zipCode,
        onValueChange = { input ->
            onValueChange(input.filter { it.isDigit() }.take(5))
        },
        label = { Text("U.S. ZIP Code") },
        placeholder = { Text("5 digits") },
        singleLine = true,
        isError = zipCode.isNotBlank() && !isZipValid,
        supportingText = {
            if (zipCode.isNotBlank() && !isZipValid) {
                Text("ZIP must be exactly 5 digits.")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

private val EMAIL_REGEX =
    Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

private val PHONE_REGEX =
    Regex("^\\d{3}[-/]\\d{3}[-/]\\d{4}$")

private val ZIP_REGEX =
    Regex("^\\d{5}$")

private fun validateEmail(value: String): Boolean =
    value.trim().matches(EMAIL_REGEX)

private fun validatePhone(value: String): Boolean =
    value.trim().matches(PHONE_REGEX)

private fun validateZipCode(value: String): Boolean =
    value.trim().matches(ZIP_REGEX)


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ContactValidatorAppPreview() {
    MaterialTheme {
        ContactValidatorApp()
    }
}
