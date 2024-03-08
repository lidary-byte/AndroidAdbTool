package widget

import MainState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import config.AppIcons
import config.AppTheme
import okio.buffer
import okio.source
import org.jetbrains.jewel.ui.component.*
import org.jetbrains.jewel.window.DecoratedWindowScope
import org.jetbrains.jewel.window.TitleBar
import org.jetbrains.jewel.window.newFullscreenControls

/**
 * @Author : lcc
 * @CreateData : 2024/2/28
 * @Description:
 */
@Composable
fun DecoratedWindowScope.BarWidget(mainState: MainState) {
//    , gradientStartColor = Color(0xFFF5D4C1)
    TitleBar(Modifier.newFullscreenControls()) {
        Row(
            Modifier.align(Alignment.Start)
                .padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
//            Icon("ic_logo.svg", "logo", EditorIcons::class.java, modifier = Modifier.size(28.dp))

            if (mainState.devices.isNotEmpty()) {
                Dropdown(Modifier.height(30.dp), menuContent = {
                    mainState.devices.forEachIndexed { index, jdbc ->
                        selectableItem(
                            selected = jdbc == mainState.selectDevice,
                            onClick = {
                                mainState.selectDevice = jdbc
                            },
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                jdbc.executeShell("getprop ro.product.model")?.source()?.buffer()
                                    ?.readUtf8()?.trim()?.let {
                                        Text(it)
                                    }
                            }
                        }
                    }
                }) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon("ic_phone.svg", "device", AppIcons::class.java)
                        Text(
                            mainState.selectDevice?.executeShell("getprop ro.product.model")?.source()?.buffer()
                                ?.readUtf8()
                                ?.trimEnd() ?: "连接设备"
                        )
                    }
                }
            }



            DefaultButton({
                mainState.refreshDevices()
            }, modifier = Modifier.padding(start = 8.dp)) {
                Text("刷新设备")
            }


        }

        Text(title)

        Row(Modifier.align(Alignment.End)) {
//            Tooltip({
//                Text("https://github.com/lidary-byte/ComposeEditor")
//            }) {
//                IconButton({
//                    Desktop.getDesktop().browse(URI.create("https://github.com/lidary-byte/ComposeEditor"))
//                }, Modifier.size(40.dp).padding(5.dp)) {
//                    Icon(
//                        "ic_github.svg",
//                        "", EditorIcons::class.java
//                    )
//                }
//            }
//
//            Tooltip({
//                when (theme) {
//                    IntUiThemes.Light -> Text("Switch to light theme with light header")
//                    IntUiThemes.LightWithLightHeader -> Text("Switch to dark theme")
//                    IntUiThemes.Dark -> Text("Switch to light theme")
//                }
//            }) {
            IconButton({
                mainState.theme = if (AppTheme.Dark == mainState.theme) {
                    AppTheme.Light
                } else {
                    AppTheme.Dark
                }
//                    mainState.theme =   when (theme) {
//                        IntUiThemes.Light -> IntUiThemes.LightWithLightHeader
//                        IntUiThemes.LightWithLightHeader -> IntUiThemes.Dark
//                        IntUiThemes.Dark -> IntUiThemes.Light
//                    }
//                    mainViewModel.themeMode(
//
//                    )
            }, Modifier.size(40.dp).padding(5.dp)) {
//                    when (mainState.theme) {
//                        AppTheme.Light -> Icon(
//                            "ic_light.svg",
//                            "Themes", EditorIcons::class.java
//                        )
//
//                        IntUiThemes.LightWithLightHeader -> Icon(
//                            "ic_light_header.svg",
//                            "Themes", EditorIcons::class.java
//                        )
//
//                        IntUiThemes.Dark -> Icon(
//                            "ic_dark.svg",
//                            "Themes", EditorIcons::class.java
//                        )
//                    }
            }
        }
//        }
    }
}