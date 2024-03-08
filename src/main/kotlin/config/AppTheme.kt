package config

import org.jetbrains.skiko.SystemTheme

/**
 * @Author : lcc
 * @CreateData : 2024/2/28
 * @Description:
 */
enum class AppTheme {
    Dark, Light, System;

    val isDark: Boolean
        get() = (if (this == System) fromSystemTheme(SystemTheme.LIGHT) else this) == Dark

    companion object {
        fun fromSystemTheme(systemTheme: SystemTheme) = if (systemTheme == SystemTheme.LIGHT) Light else Dark
    }
}