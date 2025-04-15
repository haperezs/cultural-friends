package com.haperezs.culturalfriends

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.haperezs.culturalfriends.auth.AuthViewModel
import com.haperezs.culturalfriends.finder.FinderViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    finderViewModel: FinderViewModel = viewModel()
) {
    val context = LocalContext.current
    val displayName by authViewModel.displayName.collectAsStateWithLifecycle()
    var newDisplayName by remember { mutableStateOf(displayName) }

    Column (
        modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            Text(
                text = "Your name (Shared with other users)",
            )
            TextField(
                value = newDisplayName,
                onValueChange = { newDisplayName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 0.dp, 16.dp),
            )
            Button(
                onClick = {
                    authViewModel.updateDisplayName(newDisplayName)
                    // If the user has a public marker, update the name on it too
                    finderViewModel.updatePublicMarker(newDisplayName)
                    Toast.makeText(context, "Updated name", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                Text("Update")
            }
        }
        Row {
            Button(
                onClick = {
                    authViewModel.logout()
                    navController.navigate(Screen.AuthScreen.route) {
                        popUpTo(Screen.FinderScreen.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Logout")
            }
        }
    }
}