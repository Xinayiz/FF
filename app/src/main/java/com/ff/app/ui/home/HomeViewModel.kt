package com.ff.app.ui.home

import android.app.Application
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ff.app.FFApplication
import com.ff.app.core.AiBypassEngine
import com.ff.app.config.SubscriptionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val connected: Boolean = false,
    val coreName: String = "FF Native",
    val bypassMode: String = "Balanced",
    val currentServer: String = "",
    val splitTunnel: Boolean = false,
    val blockAds: Boolean = true,
    val killswitch: Boolean = true
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val ffApp = getApplication<FFApplication>()
    private val coreManager = ffApp.coreManager
    private val aiEngine = AiBypassEngine()
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState
    lateinit var vpnPermissionLauncher: ActivityResultLauncher<Intent>

    fun toggleConnection() {
        val state = _uiState.value
        if (state.connected) {
            coreManager.stop()
            _uiState.value = state.copy(connected = false)
        } else {
            if (coreManager.isVpnPrepared()) {
                actuallyStartVpn()
            } else {
                val intent = coreManager.prepareVpn()
                intent?.let { vpnPermissionLauncher.launch(it) }
            }
        }
    }

    fun actuallyStartVpn() {
        viewModelScope.launch {
            val state = _uiState.value
            aiEngine.setMode(state.bypassMode)
            val config = SubscriptionManager.getCurrentConfig()
            coreManager.start(state.coreName, config)
            _uiState.value = state.copy(connected = true, currentServer = "Connected")
        }
    }

    fun selectCore(core: String) { _uiState.value = _uiState.value.copy(coreName = core) }
    fun selectBypassMode(mode: String) {
        _uiState.value = _uiState.value.copy(bypassMode = mode)
        aiEngine.setMode(mode)
    }
    fun toggleSplitTunnel() { _uiState.value = _uiState.value.copy(splitTunnel = !_uiState.value.splitTunnel) }
    fun toggleBlockAds() { _uiState.value = _uiState.value.copy(blockAds = !_uiState.value.blockAds) }
    fun toggleKillswitch() { _uiState.value = _uiState.value.copy(killswitch = !_uiState.value.killswitch) }
}
