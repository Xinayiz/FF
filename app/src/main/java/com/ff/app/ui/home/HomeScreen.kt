package com.ff.app.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val uiState by homeViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = if (uiState.connected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val angle by animateFloatAsState(
                    targetValue = if (uiState.connected) 360f else 0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2000, easing = LinearEasing)
                    )
                )
                Icon(
                    imageVector = if (uiState.connected) Icons.Default.Lock else Icons.Default.LockOpen,
                    contentDescription = null,
                    tint = if (uiState.connected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .size(64.dp)
                        .rotate(if (uiState.connected) angle else 0f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (uiState.connected) "Connected" else "Disconnected",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                if (uiState.connected) {
                    Text("Server: ${uiState.currentServer}", style = MaterialTheme.typography.bodyMedium)
                    Text("Core: ${uiState.coreName}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { homeViewModel.toggleConnection() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = MaterialTheme.shapes.extraLarge,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (uiState.connected) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.primary
            )
        ) {
            Text(if (uiState.connected) "Disconnect" else "Connect", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Select Core", style = MaterialTheme.typography.titleMedium)
        Row {
            listOf("FF Native", "Xray").forEach { core ->
                FilterChip(
                    selected = uiState.coreName == core,
                    onClick = { homeViewModel.selectCore(core) },
                    label = { Text(core) },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Bypass Mode", style = MaterialTheme.typography.titleMedium)
        Row {
            listOf("Speed", "Balanced", "Paranoid").forEach { mode ->
                FilterChip(
                    selected = uiState.bypassMode == mode,
                    onClick = { homeViewModel.selectBypassMode(mode) },
                    label = { Text(mode) },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Quick Toggles", style = MaterialTheme.typography.titleMedium)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            QuickToggle(Icons.Default.SplitScreen, "Split", uiState.splitTunnel) { homeViewModel.toggleSplitTunnel() }
            QuickToggle(Icons.Default.Block, "Ads", uiState.blockAds) { homeViewModel.toggleBlockAds() }
            QuickToggle(Icons.Default.Shield, "Kill", uiState.killswitch) { homeViewModel.toggleKillswitch() }
        }
    }
}

@Composable
fun QuickToggle(icon: ImageVector, label: String, checked: Boolean, onToggle: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, label)
        Switch(checked = checked, onCheckedChange = { onToggle() })
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}
