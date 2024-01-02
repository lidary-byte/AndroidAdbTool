package screen.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import tool.runExec

@Composable
fun SettingScreen(adbDefaultPath: String? = null) {
    val adbPath by remember { mutableStateOf(adbDefaultPath ?: "") }
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