package widget

import MainState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import config.AppIcons
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text

/**
 * @Author : lcc
 * @CreateData : 2024/2/28
 * @Description:
 */

@Composable
fun NavigationWidget(mainState: MainState) {
    Column(
        modifier = Modifier.width(180.dp).fillMaxHeight().background(Color(0XFFEFF5F9))
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NavigationItemWidget(mainState.selectIndex == 0, "ic_quick_future.svg", "快捷功能") {
            mainState.selectIndex = 0
        }
        NavigationItemWidget(mainState.selectIndex == 1, "ic_folder.svg", "文件管理") {
            mainState.selectIndex = 1
        }
        NavigationItemWidget(mainState.selectIndex == 2, "ic_log.svg", "Log") {
            mainState.selectIndex = 2
        }
        NavigationItemWidget(mainState.selectIndex == 3, "ic_settings.svg", "设置") {
            mainState.selectIndex = 3
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NavigationItemWidget(select: Boolean, icon: String, name: String, onClick: () -> Unit) =
    Row(
        modifier = Modifier.onClick(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 8.dp)
            .width(140.dp)
            .background(if (select) Color(0XFFADD6F7) else Color.Transparent, shape = RoundedCornerShape(8.dp))
            .padding(6.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, name, AppIcons::class.java, modifier = Modifier.size(28.dp).padding(end = 4.dp))
        Text(name)
    }