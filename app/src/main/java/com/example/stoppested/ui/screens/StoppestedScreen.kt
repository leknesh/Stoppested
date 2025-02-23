package com.example.stoppested.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.stoppested.R
import com.example.stoppested.data.Stoppested
import com.example.stoppested.data.StoppestedSuggestion
import com.example.stoppested.ui.composables.QueryCard
import com.example.stoppested.ui.composables.SingleDepartureCard
import com.example.stoppested.ui.composables.StoppestedCardView
import org.koin.androidx.compose.koinViewModel

@Composable
fun StoppestedScreen(
    modifier: Modifier = Modifier,
    stoppestedUiState: StoppestedUiState,
    onSearch: (String) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    var query by rememberSaveable { mutableStateOf("") }
    val stoppestedViewModel: StoppestedViewModel = koinViewModel()
    val suggestions by stoppestedViewModel.suggestionsState.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        when (stoppestedUiState) {
            is StoppestedUiState.Loading -> LoadingScreen(modifier)
            is StoppestedUiState.Loaded -> {
                val distance = stoppestedUiState.current?.let { stoppestedViewModel.getDistance(it) }
                ResultScreen(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = contentPadding.calculateTopPadding()),
                    stoppestedUiState.current,
                    query,
                    onQueryChange = { query = it },
                    onSearch = { onSearch(query) },
                    suggestions = suggestions,
                    onSuggestionClick = { selectedStop ->
                        stoppestedViewModel.searchDeparturesForStoppested(selectedStop.id)
                    },
                    distance = distance
                )
            }
            is StoppestedUiState.Error -> ErrorScreen(modifier, stoppestedUiState.message)
        }
    }
}

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    stoppested: Stoppested?,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    suggestions: List<StoppestedSuggestion>,
    onSuggestionClick: (StoppestedSuggestion) -> Unit,
    distance: String?
) {
    Column(modifier = modifier.fillMaxSize()) {
        StoppestedCardView {
            QueryCard(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                suggestions = suggestions,
                onSuggestionClick = { selectedStop ->
                    onSuggestionClick(selectedStop)
                    onSearch()
                }
            )
        }
        StoppestedCardView {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 8.dp)
            ) {
                Text(
                    text = stoppested?.name ?: "HER",
                    style = MaterialTheme.typography.headlineLarge,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = distance ?: "N/A",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            LazyColumn(modifier = Modifier.weight(1f)) {
                if (stoppested != null) {
                    items(stoppested.departures) { departure ->
                        SingleDepartureCard(departure)
                    }
                }
            }
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