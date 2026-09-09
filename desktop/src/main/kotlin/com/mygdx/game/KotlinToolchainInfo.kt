package com.mygdx.game

object KotlinToolchainInfo {
    @JvmStatic
    fun logStartupInfo() {
        println("Kotlin runtime ${KotlinVersion.CURRENT} running on JVM ${Runtime.version()}")
    }
}
