package page

import androidx.compose.runtime.Composable
import tool.AdbTool

@Composable
fun FileManager(deviceId: String) {
    AdbTool.fileList(deviceId)
}