package com.example.stoppested

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.stoppested.ui.screens.StoppestedViewModel
import com.example.stoppested.ui.screens.StoppestedScreen
import com.example.stoppested.ui.theme.StoppestedTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StoppestedTheme {
                KoinAndroidContext {
                    App()
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun App(stoppestedViewModel: StoppestedViewModel = koinViewModel<StoppestedViewModel>() ) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        val permissionsState = rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        var showRationale by rememberSaveable { mutableStateOf(false) }
        val permissionsGranted = rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(permissionsState) {
            permissionsState.launchMultiplePermissionRequest()
        }

        permissionsGranted.value = permissionsState.allPermissionsGranted
        if (permissionsState.shouldShowRationale) {
            showRationale = true
        }

        if (showRationale) {
            PermissionRationaleDialog(
                onDismiss = { showRationale = false },
                onConfirm = {
                    showRationale = false
                    permissionsState.launchMultiplePermissionRequest()
                }
            )
        }

        if (!permissionsGranted.value) {
            LaunchedEffect(Unit) {

            }
        } else {
            LaunchedEffect(Unit) {
                stoppestedViewModel.updateLocation()
            }
        }


        StoppestedScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            onSearch = { query -> {} },
            stoppestedUiState = stoppestedViewModel.uiState,
            placeName = "Oslo"
        )

    }
}

@Composable
fun PermissionRationaleDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.permission_required)) },
        text = { Text(stringResource(R.string.location_permission_required_long)) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(R.string.grant_permission))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}