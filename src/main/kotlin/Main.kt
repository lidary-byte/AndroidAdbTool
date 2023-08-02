// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import bean.createMainNavData
import page.Logcat
import page.QuickPage
import page.SettingPage
import res.defaultBgColor
import tool.AdbTool


@Composable
@Preview
fun App() {
    val adbPath = AdbTool.adbPath()
    var selectItem by remember { mutableStateOf(0) }
    Scaffold(bottomBar = {
        MainNav {
            selectItem = it
        }
    }) {
        // NavigationRail 宽度72.dp
        Box(
            modifier = Modifier.background(defaultBgColor).fillMaxHeight().fillMaxWidth().padding(start = 72.dp)
        ) {
            when (selectItem) {
                0 -> QuickPage()
                2 -> Logcat()
                3 -> SettingPage(adbPath)
            }
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "tool.AdbTool",
        visible = true,
        state = WindowState(size = DpSize(width = 1200.dp, height = 900.dp))
    ) {
        App()
    }
}


@Composable
private fun MainNav(onSelectItem: (Int) -> Unit) {
    var navigationIndex by remember { mutableStateOf(0) }
    NavigationRail {
        createMainNavData().forEachIndexed { index, mainNavBean ->
            MainNavItem(navigationIndex, index, mainNavBean.svgName, mainNavBean.labelText) {
                navigationIndex = it
                onSelectItem.invoke(navigationIndex)
            }
        }
    }
}

/**
 * @param selectedPosition 当前选中的position
 * @param mePosition 自己的position
 */
@Composable
private fun MainNavItem(
    selectedPosition: Int,
    mePosition: Int,
    svgPath: String,
    labelText: String,
    onClick: (Int) -> Unit
) {
    NavigationRailItem(selectedPosition == mePosition, {
        onClick.invoke(mePosition)
    }, {
        Image(
            painterResource(svgPath), "", modifier = Modifier.width(24.dp).height(24.dp)
        )
    }, label = {
        Text(labelText)
    }, alwaysShowLabel = false, selectedContentColor = Color.Black)
}