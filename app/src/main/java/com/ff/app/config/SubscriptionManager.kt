package com.ff.app.config

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

object SubscriptionManager {
    private val configs = mutableListOf<String>()
    private var currentConfig = ""

    suspend fun loadFromUrl(url: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val content = URL(url).readText()
            val parsed = content.lines()
                .filter { it.startsWith("vmess://") || it.startsWith("vless://") || it.startsWith("ss://") || it.startsWith("trojan://") }
                .map { ConfigParser.parse(it) }
            configs.clear()
            configs.addAll(parsed)
            if (parsed.isNotEmpty()) currentConfig = parsed.first()
            true
        } catch (e: Exception) { false }
    }

    fun addConfig(config: String) { configs.add(config) }
    fun setCurrentConfig(config: String) { currentConfig = config }
    fun getCurrentConfig(): String = currentConfig
    fun getAllConfigs(): List<String> = configs.toList()
}
