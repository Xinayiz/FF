package com.ff.app.core

abstract class VpnCore {
    abstract fun start(config: String)
    abstract fun stop()
    abstract fun pause()
    abstract fun resume()
    abstract fun isRunning(): Boolean
}
