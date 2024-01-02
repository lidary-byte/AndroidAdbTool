package bean

import ROUTER_FILE_MANAGER_SCREEN
import ROUTER_LOGCAT_SCREEN
import ROUTER_QUICK_SCREEN
import ROUTER_SETTING_SCREEN
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LogoDev
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * @Author : lcc
 * @CreateData : 2024/1/2
 * @Description:
 */
data class HomeTab(val name: String, val icon: ImageVector, val router: String)


fun createHomeTab() = buildList {
    add(HomeTab("快捷功能", Icons.Default.Home, ROUTER_QUICK_SCREEN))
    add(HomeTab("文件管理", Icons.Default.FileCopy, ROUTER_FILE_MANAGER_SCREEN))
    add(HomeTab("Logcat", Icons.Default.LogoDev, ROUTER_LOGCAT_SCREEN))
    add(HomeTab("设置", Icons.Default.Settings, ROUTER_SETTING_SCREEN))
}