package com.ff.app.config

import android.util.Base64

object ConfigParser {
    fun parse(raw: String): String {
        val trimmed = raw.trim()
        return when {
            trimmed.startsWith("vmess://") -> {
                try {
                    val b64 = trimmed.removePrefix("vmess://")
                    String(Base64.decode(b64, Base64.DEFAULT))
                } catch (e: Exception) { "{}" }
            }
            trimmed.startsWith("vless://") -> trimmed
            trimmed.startsWith("ss://") -> trimmed
            trimmed.startsWith("trojan://") -> trimmed
            else -> trimmed
        }
    }
}
