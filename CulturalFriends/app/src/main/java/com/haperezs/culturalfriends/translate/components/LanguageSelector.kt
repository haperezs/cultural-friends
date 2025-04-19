package com.haperezs.culturalfriends.translate.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.haperezs.culturalfriends.model.Language

@Composable
fun LanguageSelector(
    selectedLanguage: Language?,
    languages: List<Language>,
    onSelect: (Language) -> Unit,
    modifier: Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        OutlinedButton(
            onClick = {
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
                        Text(language.name)
                    },
                    onClick = {
                        onSelect(language)
                        expanded = false
                    }
                )
            }
        }
    }
}