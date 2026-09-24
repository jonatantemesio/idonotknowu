package com.idonotknowu.callblocker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.activity.compose.BackHandler
import com.idonotknowu.callblocker.data.BlockedCall
import java.text.DateFormat
import java.util.Date

@Composable
fun BlockedListScreen(calls: List<BlockedCall>, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    val format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        OutlinedButton(onClick = onBack) { Text("Voltar") }
        Text(
            "Total bloqueadas: ${calls.size}",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(vertical = 16.dp),
        )
        LazyColumn {
            items(calls, key = { it.id }) { call ->
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(call.number, style = MaterialTheme.typography.bodyLarge)
                    Text(format.format(Date(call.timestamp)), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
