package com.haperezs.culturalfriends.translate

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.haperezs.culturalfriends.Screen

@Composable
fun TranslateScreen(
    navController: NavController
) {
    var inputText by remember { mutableStateOf("") }
    var outputText by remember { mutableStateOf("Translation") }
    var sourceLang by remember { mutableStateOf("English") }
    var targetLang by remember { mutableStateOf("Spanish") }

    Column(
        modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = {
                    Log.d(javaClass.simpleName, "Change language")
                },
                modifier = Modifier
                            .padding(4.dp)
                            .weight(1f)
            ) {
                Text(sourceLang)
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Source language")
            }

            IconButton(onClick = {
                val temp = sourceLang
                sourceLang = targetLang
                targetLang = temp
            }) {
                Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = "Swap languages")
            }

            OutlinedButton(
                onClick = {
                    Log.d(javaClass.simpleName, "Change language")
                },
                modifier = Modifier
                            .padding(4.dp)
                            .weight(1f)
            ) {
                Text(targetLang)
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Source language")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
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
                value = outputText,
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
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Translate")
        }
    }
}