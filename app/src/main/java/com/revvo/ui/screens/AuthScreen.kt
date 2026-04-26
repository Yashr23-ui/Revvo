package com.revvo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revvo.ui.theme.RevvoDark
import com.revvo.ui.theme.RevvoGray
import com.revvo.ui.theme.RevvoOrange
import com.revvo.ui.theme.RevvoSurface
import com.revvo.ui.theme.RevvoSurfaceLight
import com.revvo.ui.theme.RevvoWhite
import com.revvo.viewmodel.AuthFormState
import com.revvo.viewmodel.AuthViewModel

/**
 * Sign-in / sign-up screen. Shown by [com.revvo.MainActivity] when auth state is
 * `LoggedOut`. On successful sign-in the `authState` flow flips to `LoggedIn`, MainActivity
 * notices, and swaps in the main nav graph — this screen never has to know it succeeded.
 *
 * Google sign-in button is wired but does nothing until the SHA-1 fingerprint is registered
 * with Firebase and we hook up the Credential Manager flow (next step).
 */
@Composable
fun AuthScreen(authViewModel: AuthViewModel) {
    val formState by authViewModel.formState.collectAsState()
    var selectedTab by rememberSaveable { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize().background(RevvoDark)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(80.dp)) }

            // Brand header
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "REVVO",
                        style = MaterialTheme.typography.displayLarge,
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = RevvoOrange,
                        letterSpacing = 4.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "RIDE TOGETHER",
                        style = MaterialTheme.typography.labelSmall,
                        color = RevvoGray,
                        letterSpacing = 4.sp
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Sign-in / Sign-up tabs
            item {
                Surface(
                    color = RevvoSurface.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = androidx.compose.ui.graphics.Color.Transparent,
                        contentColor = RevvoOrange
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = {
                                selectedTab = 0
                                authViewModel.clearError()
                            },
                            text = {
                                Text(
                                    "SIGN IN",
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedTab == 0) RevvoOrange else RevvoGray
                                )
                            }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = {
                                selectedTab = 1
                                authViewModel.clearError()
                            },
                            text = {
                                Text(
                                    "SIGN UP",
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedTab == 1) RevvoOrange else RevvoGray
                                )
                            }
                        )
                    }
                }
            }

            item {
                if (selectedTab == 0) {
                    SignInForm(
                        formState = formState,
                        onSubmit = { email, password -> authViewModel.signIn(email, password) }
                    )
                } else {
                    SignUpForm(
                        formState = formState,
                        onSubmit = { name, email, password ->
                            authViewModel.signUp(name, email, password)
                        }
                    )
                }
            }

            item { GoogleSignInButton(enabled = formState !is AuthFormState.Submitting) }

            item { Spacer(modifier = Modifier.height(48.dp)) }
        }
    }
}

@Composable
private fun SignInForm(
    formState: AuthFormState,
    onSubmit: (email: String, password: String) -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        AuthTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            keyboardType = KeyboardType.Email
        )
        AuthTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            keyboardType = KeyboardType.Password,
            isPassword = true
        )
        FormError(formState)
        SubmitButton(
            label = "SIGN IN",
            isSubmitting = formState is AuthFormState.Submitting,
            onClick = { onSubmit(email, password) }
        )
    }
}

@Composable
private fun SignUpForm(
    formState: AuthFormState,
    onSubmit: (name: String, email: String, password: String) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        AuthTextField(
            value = name,
            onValueChange = { name = it },
            label = "Your name"
        )
        AuthTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            keyboardType = KeyboardType.Email
        )
        AuthTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password (min 6 chars)",
            keyboardType = KeyboardType.Password,
            isPassword = true
        )
        FormError(formState)
        SubmitButton(
            label = "CREATE ACCOUNT",
            isSubmitting = formState is AuthFormState.Submitting,
            onClick = { onSubmit(name, email, password) }
        )
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = RevvoGray) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = RevvoSurfaceLight,
            unfocusedContainerColor = RevvoSurface.copy(alpha = 0.4f),
            focusedTextColor = RevvoWhite,
            unfocusedTextColor = RevvoWhite,
            focusedIndicatorColor = RevvoOrange,
            unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
            cursorColor = RevvoOrange
        )
    )
}

@Composable
private fun FormError(formState: AuthFormState) {
    if (formState is AuthFormState.Error) {
        Text(
            formState.message,
            style = MaterialTheme.typography.bodySmall,
            color = androidx.compose.ui.graphics.Color(0xFFFF6B6B)
        )
    }
}

@Composable
private fun SubmitButton(label: String, isSubmitting: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = !isSubmitting,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = RevvoOrange)
    ) {
        if (isSubmitting) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = RevvoWhite,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                label,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
private fun GoogleSignInButton(enabled: Boolean) {
    // PREVIEW: wired in the next pass once SHA-1 is registered with Firebase.
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedButton(
            onClick = { /* TODO: Credential Manager flow */ },
            enabled = enabled,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "CONTINUE WITH GOOGLE  (coming soon)",
                color = RevvoGray,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
