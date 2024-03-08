// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import config.AppTheme
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.intui.standalone.theme.darkThemeDefinition
import org.jetbrains.jewel.intui.standalone.theme.lightThemeDefinition
import org.jetbrains.jewel.intui.window.decoratedWindow
import org.jetbrains.jewel.intui.window.styling.dark
import org.jetbrains.jewel.intui.window.styling.light
import org.jetbrains.jewel.ui.ComponentStyling
import org.jetbrains.jewel.window.DecoratedWindow
import org.jetbrains.jewel.window.styling.TitleBarStyle
import screen.file_manager.FileMangerScreen
import screen.logcat.LogcatScreen
import screen.quick.QuickScreen
import screen.setting.SettingScreen
import widget.BarWidget
import widget.NavigationWidget
import javax.swing.UIManager


fun main() = application {
    /// 让swing的ui看着比较现代化
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())


    val mainState = remember { MainState() }

    val theme = if (AppTheme.System.isDark) {
        JewelTheme.darkThemeDefinition()
    } else {
        JewelTheme.lightThemeDefinition()
    }

    val titleBarStyle = if (AppTheme.System.isDark) {
        TitleBarStyle.dark()
    } else {
        TitleBarStyle.light()
    }

    IntUiTheme(theme, ComponentStyling.decoratedWindow(titleBarStyle = titleBarStyle), false) {
        DecoratedWindow(
            state = rememberWindowState(
                position = WindowPosition.Aligned(Alignment.Center),
                width = 1400.dp,
                height = 800.dp
            ),
            onCloseRequest = ::exitApplication,
            title = "Adb Tools"
        ) {
            BarWidget(mainState)
            Row(modifier = Modifier.fillMaxSize()) {
                NavigationWidget(mainState)
                Box(modifier = Modifier.fillMaxSize()) {
                    when (mainState.selectIndex) {
                        0 -> QuickScreen(mainState)
                        1 -> FileMangerScreen(mainState)
                        2 -> LogcatScreen(mainState)
                        3 -> SettingScreen()
                    }
                }
            }
        }
    }
}