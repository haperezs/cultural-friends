package com.haperezs.culturalfriends.finder

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.haperezs.culturalfriends.auth.AuthViewModel

@Composable
fun FinderScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val displayName by authViewModel.displayName.collectAsStateWithLifecycle()

    Column {
        Text(
            text = "Welcome $displayName",
        )
        Text(
            text = "Finder Screen",
        )
    }
}