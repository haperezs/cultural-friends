package com.haperezs.culturalfriends.settings

import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.haperezs.culturalfriends.navigation.Screen
import com.haperezs.culturalfriends.auth.AuthViewModel
import com.haperezs.culturalfriends.finder.FinderViewModel
import com.haperezs.culturalfriends.model.Language
import com.haperezs.culturalfriends.translate.TranslateViewModel
import com.haperezs.culturalfriends.translate.components.LanguageSelector

@Composable
fun SettingsScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    finderViewModel: FinderViewModel,
    translateViewModel: TranslateViewModel
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val displayName by authViewModel.displayName.collectAsStateWithLifecycle()
    val languages by translateViewModel.languages.collectAsStateWithLifecycle()
    val publicMarker by finderViewModel.publicMarker.collectAsStateWithLifecycle()

    var language by remember { mutableStateOf<Language?>(null) }
    var newDisplayName by remember { mutableStateOf(displayName) }

    LaunchedEffect(publicMarker) {
        if (publicMarker != null && language == null) {
            language = languages.firstOrNull { it.language == publicMarker!!.language }
        }
    }

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
            LanguageSelector(
                selectedLanguage = language,
                languages = languages,
                onSelect = {
                    language = it
                    Log.d(javaClass.simpleName, "Changed user language selector to $it")
                },
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 16.dp)
            )
            Button(
                onClick = {
                    authViewModel.updateDisplayName(newDisplayName)
                    // If the user has a public marker, update the name on it too
                    finderViewModel.updatePublicMarker(newDisplayName, language?.language ?: "en")
                    keyboardController?.hide()
                    Toast.makeText(context, "Updated public info", Toast.LENGTH_SHORT).show()
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
                        popUpTo(0)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Logout")
            }
        }
    }
}