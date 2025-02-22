package com.example.stoppested

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.stoppested.ui.screens.StoppestedViewModel
import com.example.stoppested.ui.screens.StoppestedScreen
import com.example.stoppested.ui.theme.StoppestedTheme
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

@Composable
fun App(stoppestedViewModel: StoppestedViewModel = koinViewModel<StoppestedViewModel>() ) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) { innerPadding ->

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
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StoppestedTheme {
        Greeting("Android")
    }
}