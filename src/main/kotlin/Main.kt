// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import screen.home.Home

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "tool.AdbTool",
        visible = true,
        state = WindowState(size = DpSize(width = 1200.dp, height = 900.dp))
    ) {
        MaterialTheme {
            Home()
        }
    }
}