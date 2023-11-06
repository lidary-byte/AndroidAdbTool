package screen.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import screen.file_manager.FileMangerScreen
import screen.logcat.LogcatScreen
import screen.quick.QuickScreen
import screen.setting.SettingScreen

data class QuickTab(val deviceId: String) : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Home)
            return remember {
                TabOptions(
                    index = 0u,
                    title = "快捷功能",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(QuickScreen(deviceId))
    }
}

object FileManagerTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.FileCopy)
            return remember {
                TabOptions(
                    index = 1u,
                    title = "文件管理",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(FileMangerScreen(""))
    }
}

object LogcatTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.LogoDev)
            return remember {
                TabOptions(
                    index = 2u,
                    title = "Logcat",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(LogcatScreen(""))
    }
}


object SettingTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Settings)
            return remember {
                TabOptions(
                    index = 3u,
                    title = "设置",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(SettingScreen())
    }
}