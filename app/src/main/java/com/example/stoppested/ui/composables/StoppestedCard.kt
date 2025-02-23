package com.example.stoppested.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.stoppested.R
import com.example.stoppested.data.Departure
import com.example.stoppested.data.StoppestedSuggestion
import com.example.stoppested.data.getDrawable


@Composable
fun StoppestedCardView(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            content()
        }
    }
}

@Composable
fun SingleDepartureCard(departure: Departure, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = departure.transportType.getDrawable()),
            contentDescription = departure.lineName
        )
        Text(
            text = "${departure.lineName} (${departure.lineCode})",
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Column {
            Text(
                text = departure.scheduledDeparture,
                style = MaterialTheme.typography.bodyMedium
            )
            if (departure.expectedDeparture != null && departure.expectedDeparture != departure.scheduledDeparture) {
                Text(
                    text = departure.expectedDeparture,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Red
                )
            }

        }

    }
}

@Composable
fun QueryCard(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    suggestions: List<StoppestedSuggestion>,
    onSuggestionClick: (StoppestedSuggestion) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        TextField(
            value = query,
            onValueChange = {
                onQueryChange(it)
                if (it.isNotEmpty()) {
                    onSearch()
                    expanded = true
                } else {
                    expanded = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.search_location)) },
            singleLine = true
        )
        if (expanded && suggestions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                items(suggestions) { suggestion ->
                    Text(
                        text = suggestion.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                onSuggestionClick(suggestion)
                                onQueryChange("")
                                expanded = false
                            }
                    )
                }
            }
        }
    }
}