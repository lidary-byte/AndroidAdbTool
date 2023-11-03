package screen.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bean.FileBean
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tool.AdbTool
import tool.runExec
import tool.runExecAndAdbToBuffer

class SettingScreen(val adbDefaultPath: String? = null) : Screen {
    @Composable
    override fun Content() {
        var adbPath by remember { mutableStateOf(adbDefaultPath ?: "") }
        var testAdb by remember { mutableStateOf("") }
        Column {
            Text("设置Adb路径", fontSize = 18.sp)
            Row {
                Text(adbPath)
                Button({
                    testAdb = "adb version".runExec()
                }) {
                    Text("测试")
                }
            }
            Text(testAdb, color = if (testAdb.contains("Version")) Color.Green else Color.Red)
        }
    }
}