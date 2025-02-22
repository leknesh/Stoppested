package com.example.stoppested.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.stoppested.R
import com.example.stoppested.data.Stoppested

@Composable
fun StoppestedScreen(
    modifier: Modifier = Modifier,
    stoppestedUiState: StoppestedUiState,
    onSearch: (String) -> Unit,
    placeName: String? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    var query by rememberSaveable { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize()) {
        when (stoppestedUiState) {
            is StoppestedUiState.Loading -> LoadingScreen(modifier)
            is StoppestedUiState.Loaded -> ResultScreen(
                stoppestedUiState.current,
                query,
                placeName,
                onQueryChange = { query = it },
                onSearch = { onSearch(query) },
                modifier.padding(top = contentPadding.calculateTopPadding())
            )

            is StoppestedUiState.Error -> ErrorScreen(modifier, stoppestedUiState.message)
        }
    }
}

@Composable
fun ResultScreen(
    stoppested: Stoppested,
    query: String,
    placeName: String?,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn (modifier = modifier) {
        item {
            Text(text = "Stoppesteder")
        }
        item {
            Text(text = stoppested.name)
        }
    }
}


@Composable
fun ErrorScreen(modifier: Modifier, message: String) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "error"
        )
        Text(
            text = message,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun LoadingScreen(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(1f),
        contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant

        )
    }
}