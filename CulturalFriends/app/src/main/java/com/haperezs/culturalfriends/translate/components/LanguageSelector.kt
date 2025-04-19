package com.haperezs.culturalfriends.translate.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.haperezs.culturalfriends.model.Language

@Composable
fun LanguageSelector(
    selectedLanguage: Language?,
    languages: List<Language?>,
    onSelect: (Language) -> Unit,
    modifier: Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        OutlinedButton(
            onClick = {
                Log.d("LanguageSelector", "Change language")
                expanded = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedLanguage?.name ?: "English")
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Language")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    text = {
                        if (language != null) {
                            Text(language.name)
                        }
                    },
                    onClick = {
                        if (language != null) {
                            onSelect(language)
                        }
                        expanded = false
                    }
                )
            }
        }
    }
}