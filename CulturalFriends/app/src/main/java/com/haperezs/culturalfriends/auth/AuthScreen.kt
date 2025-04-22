package com.haperezs.culturalfriends.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavController
import com.haperezs.culturalfriends.navigation.Screen
import com.haperezs.culturalfriends.finder.FinderViewModel

@Composable
fun AuthScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    finderViewModel: FinderViewModel
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }

    Column (
        modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        TextField (
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 0.dp, 0.dp, 16.dp),
        )
        TextField (
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 0.dp, 0.dp, 16.dp),
        )
        Row (
            modifier = Modifier
                        .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column {
                Button (
                    onClick = {
                        authViewModel.login(email, password) { success, error ->
                            message = if (success) "Logged in!" else error
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            if (success){
                                navController.navigate(Screen.FinderScreen.route){
                                    popUpTo(Screen.AuthScreen.route) { inclusive = true }
                                }
                            }
                        }
                        keyboardController?.hide()
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text("Login")
                }
                OutlinedButton (
                    onClick = {
                        authViewModel.register(email, password) { success, error ->
                            message = if (success) "Registered!" else error
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            if (success){
                                finderViewModel.createPublicMarker()
                                navController.navigate(Screen.FinderScreen.route){
                                    popUpTo(Screen.AuthScreen.route) { inclusive = true }
                                }
                            }
                        }
                        keyboardController?.hide()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 16.dp),
                ) {
                    Text("Register")
                }
            }
        }
    }
}
