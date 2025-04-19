package com.haperezs.culturalfriends.translate

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.haperezs.culturalfriends.translate.components.LanguageSelector

@Composable
fun TranslateScreen(
    navController: NavController,
    translateViewModel: TranslateViewModel,
) {
    val languages by translateViewModel.languages.collectAsStateWithLifecycle()
    val inputText by translateViewModel.inputText.collectAsStateWithLifecycle()
    val outputText by translateViewModel.outputText.collectAsStateWithLifecycle()
    val sourceLang by translateViewModel.sourceLang.collectAsStateWithLifecycle()
    val targetLang by translateViewModel.targetLang.collectAsStateWithLifecycle()
    val isLoading by translateViewModel.isLoading.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            LanguageSelector(
                selectedLanguage = sourceLang,
                languages = languages,
                onSelect = { language ->
                    translateViewModel.updateSourceLanguage(language)
                },
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = {
                translateViewModel.swapSourceAndTargetLanguage()
            }) {
                Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = "Swap languages")
            }

            LanguageSelector(
                selectedLanguage = targetLang,
                languages = languages,
                onSelect = { language ->
                    translateViewModel.updateTargetLanguage(language)
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            TextField(
                value = inputText?: "",
                onValueChange = { translateViewModel.changeInputText(it) },
                modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                placeholder = { Text("Enter text") },
                singleLine = false,
                maxLines = 6,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            TextField(
                value = when {
                    isLoading -> "Translating..."
                    else -> "$outputText"
                },
                onValueChange = { },
                modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                enabled = false,
                singleLine = false,
                maxLines = 6,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button (
            onClick = {
                Log.d(javaClass.simpleName, "Translate")
                translateViewModel.translateText()
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Translate")
        }
    }
}