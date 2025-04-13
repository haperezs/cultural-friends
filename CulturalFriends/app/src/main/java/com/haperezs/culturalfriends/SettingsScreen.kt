package com.haperezs.culturalfriends

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.haperezs.culturalfriends.auth.AuthViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    Button (
        onClick = {
            authViewModel.logout()
            navController.navigate(Screen.AuthScreen.route){
                popUpTo(Screen.FinderScreen.route) { inclusive = true }
            }
        },
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Text("Logout")
    }
}