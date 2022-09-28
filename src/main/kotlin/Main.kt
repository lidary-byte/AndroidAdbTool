// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    Scaffold(bottomBar = {
        NavigationRail {
            NavigationRailItem(true, {}, {
                Icon(
                    painterResource("ic_quick_future.svg"), "", modifier = Modifier.width(24.dp)
                        .height(24.dp)
                )
            })
            NavigationRailItem(false, {}, {
                Icon(
                    painterResource("ic_folder.svg"), "", modifier = Modifier.width(24.dp)
                        .height(24.dp)
                )
            })
            NavigationRailItem(false, {}, {
                Icon(
                    painterResource("ic_log.svg"), "", modifier = Modifier.width(24.dp)
                        .height(24.dp)
                )
            })
            NavigationRailItem(false, {}, {
                Icon(
                    painterResource("ic_settings.svg"), "", modifier = Modifier.width(24.dp)
                        .height(24.dp)
                )
            })
        }
    }) {

    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "AdbTool") {
        App()
    }
}
