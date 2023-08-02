package page

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bean.DeviceInfo
import okio.buffer
import okio.source
import res.randomColor
import tool.AdbTool
import tool.runExec
import tool.ttfFontFamily
import widget.rememberForeverLazyListState
import kotlin.random.Random
import kotlin.random.nextInt

/**
 * 快捷功能
 */
@Composable
fun QuickPage() {
    println("===============状态111")
    var dialogTitle by remember { mutableStateOf("") }
    var dialogContent by remember { mutableStateOf("") }

    // 当前选中的设备id
    var device by remember { mutableStateOf("") }

    if (dialogContent.isNotEmpty()) {
        MessageDialog(dialogTitle, dialogContent) {
            dialogTitle = ""
            dialogContent = ""
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxHeight().padding(horizontal = 8.dp, vertical = 12.dp).fillMaxWidth()
        , state = rememberForeverLazyListState(key = "QuickPage")
    ) {
        // 前后加个间距
        item {
            Spacer(modifier = Modifier.height(8.dp))
            ConnectDevices {
                device = it ?: ""
            }
            Spacer(modifier = Modifier.height(16.dp))
            CommonFunction(device) { title, content ->
                dialogTitle = title
                dialogContent = content
            }
            Spacer(modifier = Modifier.height(16.dp))
            AboutApp()
            Spacer(modifier = Modifier.height(16.dp))
            AboutSystem()
            Spacer(modifier = Modifier.height(16.dp))
            AboutKeyBoard(device)
            Spacer(modifier = Modifier.height(16.dp))
            ScreenInput()
            Spacer(modifier = Modifier.height(16.dp))

        }
//
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ConnectDevices(deviceCallback: (String?) -> Unit) {
    // 拿到已经连接的所有设备
    val devices = mutableStateListOf<DeviceInfo>()


    var showDeviceItem by remember { mutableStateOf(false) }
    val size by animateDpAsState(
        targetValue = if (showDeviceItem) 120.dp else 0.dp
    )
    // 箭头旋转动画
    val arrowAnim by animateFloatAsState(if (showDeviceItem) -180f else 0f)

    var selectIndexDevice by remember { mutableStateOf(0) }

    fun devicesList() {
        val p = Runtime.getRuntime().exec("adb devices")
        p.inputStream.source().buffer().use {
            while (true) {
                val line = it.readUtf8Line() ?: break
                if (line.contains("List of devices attached") || line.isBlank()) {
                    continue
                }
                if (line.contains("device")) {
                    val deviceLine = line.split("\t")
                    if (deviceLine.isEmpty()) {
                        continue
                    }
                    val device = deviceLine[0]
                    val deviceName = "adb -s $device shell getprop ro.product.brand".runExec()
                    val deviceModel = "adb -s $device shell getprop ro.product.model".runExec()
                    devices.add(DeviceInfo(deviceName, deviceModel, device))
                }
            }
        }
    }
    devicesList()

    BaseQuick("连接设备", Color(155, 203, 99)) {
        val deviceName = if (devices.isNotEmpty()) {
            val firstDevice = devices[selectIndexDevice]
            // 获取设备品牌
            deviceCallback.invoke(firstDevice.device)
            "${firstDevice.deviceName}  ${firstDevice.deviceModel}"
        } else {
            deviceCallback.invoke(null)
            "连接设备"
        }

        TextButton(
            {
                showDeviceItem = !showDeviceItem
            },
            shape = RoundedCornerShape(19.dp), border = BorderStroke(1.dp, Color.Black),
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            Text(deviceName, color = Color.Black)
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = "",
                tint = Color.Black,
                modifier = Modifier.graphicsLayer {
                    rotationX = arrowAnim
                })
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(modifier = Modifier.height(size).padding(horizontal = 6.dp)) {
            items(devices.size) {
                val device = devices[it]
                Row(modifier = Modifier.clickable {
                    selectIndexDevice = it
                    showDeviceItem = false
                    deviceCallback.invoke(device.device)
                }.padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "${device.deviceName}  ${device.deviceModel}",
                        color = if (deviceName.contains(device.deviceName) || deviceName.contains(device.deviceModel)) Color(
                            139,
                            195,
                            74
                        ) else Color.Black, modifier = Modifier.weight(1f)
                    )
                    if (deviceName.contains(device.deviceName) || deviceName.contains(device.deviceModel)) {
                        Icon(Icons.Default.Check, contentDescription = "", tint = Color(139, 195, 74))
                    }
                }
            }
        }
    }
}

/**
 * 屏幕输入
 */
@Composable
private fun ScreenInput() {
    BaseQuick("屏幕输入", color = Color(146, 106, 255)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            QuickItem(0xe795, "向上滑动", modifier = Modifier.weight(1f))
            QuickItem(0xe603, "向下滑动", modifier = Modifier.weight(1f))
            QuickItem(0xe60a, "向左滑动", modifier = Modifier.weight(1f))
            QuickItem(0xe6ca, "向右滑动", modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(14.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            QuickItem(0xe697, "屏幕点击", modifier = Modifier.weight(1f))
            QuickItem(modifier = Modifier.weight(1f))
            QuickItem(modifier = Modifier.weight(1f))
            QuickItem(modifier = Modifier.weight(1f))
        }
    }
}

/**
 * 按键相关
 */
@Composable
private fun AboutKeyBoard(deviceId: String) {
    BaseQuick("按键相关", color = Color(158, 176, 184)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            QuickItem(0xe68e, "Home键", modifier = Modifier.weight(1f).clickable { AdbTool.pressHome(deviceId) })
            QuickItem(0xe616, "Back键", modifier = Modifier.weight(1f).clickable { AdbTool.pressBack(deviceId) })
            QuickItem(0xe605, "Menu键", modifier = Modifier.weight(1f).clickable { AdbTool.pressMenu(deviceId) })
            QuickItem(0xe615, "Power键", modifier = Modifier.weight(1f).clickable { AdbTool.pressPower(deviceId) })
        }
        Spacer(modifier = Modifier.height(14.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            QuickItem(0xe76e, "增加音量", modifier = Modifier.weight(1f).clickable { AdbTool.pressVolumeUp(deviceId) })
            QuickItem(
                0xe771,
                "降低音量",
                modifier = Modifier.weight(1f).clickable { AdbTool.pressVolumeDown(deviceId) })
            QuickItem(0xe612, "静音", modifier = Modifier.weight(1f).clickable { AdbTool.pressVolumeMute(deviceId) })
            QuickItem(0xe658, "切换应用", modifier = Modifier.weight(1f).clickable { AdbTool.pressSwitchApp(deviceId) })
        }
        Spacer(modifier = Modifier.height(14.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            QuickItem(0xe796, "遥控器", modifier = Modifier.weight(1f))
            QuickItem(modifier = Modifier.weight(1f))
            QuickItem(modifier = Modifier.weight(1f))
            QuickItem(modifier = Modifier.weight(1f))
        }
    }
}

/**
 * 系统相关
 */
@Composable
private fun AboutSystem() {
    BaseQuick("系统相关", color = Color(255, 193, 7)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            QuickItem(0xe695, "开始录屏", modifier = Modifier.weight(1f))
            QuickItem(0xe71d, "结束录屏保存到电脑", modifier = Modifier.weight(1f))
            QuickItem(0xe881, "查看AndroidId", modifier = Modifier.weight(1f))
            QuickItem(0xe617, "查看系统版本", modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(14.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            QuickItem(0xe632, "查看IP地址", modifier = Modifier.weight(1f))
            QuickItem(0xe65d, "查看Mac地址", modifier = Modifier.weight(1f))
            QuickItem(0xe6b2, "重启手机", modifier = Modifier.weight(1f))
            QuickItem(0xe61e, "查看系统属性", modifier = Modifier.weight(1f))
        }
    }
}

/**
 * 应用相关
 */
@Composable
private fun AboutApp() {
    BaseQuick("应用相关", color = Color(0, 188, 212)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            QuickItem(0xe740, "卸载应用", modifier = Modifier.weight(1f))
            QuickItem(0xe6af, "启动应用", modifier = Modifier.weight(1f))
            QuickItem(0xe875, "停止运行", modifier = Modifier.weight(1f))
            QuickItem(0xe7d6, "重启应用", modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(14.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            QuickItem(0xe613, "清除数据", modifier = Modifier.weight(1f))
            QuickItem(0xe633, "清除数据并重启应用", modifier = Modifier.weight(1f))
            QuickItem(0xe647, "重置权限", modifier = Modifier.weight(1f))
            QuickItem(0xe628, "重置权限并重启应用", modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(14.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            QuickItem(0xe66c, "授权所有权限", modifier = Modifier.weight(1f))
            QuickItem(0xe76d, "查看应用安装路径", modifier = Modifier.weight(1f))
            QuickItem(0xe60e, "保存Apk到电脑", modifier = Modifier.weight(1f))
            // 补全一个
            QuickItem(modifier = Modifier.weight(1f))
        }
    }
}

/**
 * 常用功能
 */
@Composable
private fun CommonFunction(device: String, onClick: (title: String, content: String) -> Unit) {
    BaseQuick("常用功能", color = Color(255, 152, 0)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            QuickItem(0xe693, "安装应用", modifier = Modifier.weight(1f))
            QuickItem(0xe816, "输入文本", modifier = Modifier.weight(1f))
            QuickItem(0xe931, "截图保存到电脑", modifier = Modifier.weight(1f))
            QuickItem(0xe607, "查看当前Activity", modifier = Modifier.weight(1f).clickable {
                onClick.invoke("查看当前Activity", "adb -s $device shell dumpsys window | grep mCurrentFocus".runExec())
            })
        }
    }
}

@Composable
private fun BaseQuick(title: String, color: Color = Color.Transparent, content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier.background(Color.White).fillMaxWidth(),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(0.5.dp, Color(220, 220, 220)),  // 边框
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(
                    modifier = Modifier.width(4.dp).height(16.dp).clip(RoundedCornerShape(8.dp)).background(color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(title)
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun QuickItem(ttf: Int? = null, title: String? = null, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        if (ttf == null || title == null) return@Column
        Box(
            modifier = Modifier.clip(RoundedCornerShape(14.dp)).background(
                randomColor[Random.nextInt(
                    IntRange(
                        0, randomColor.size - 1
                    )
                )]
            ),
        ) {
            Text(
                text = "${Char(ttf)}",
                fontFamily = ttfFontFamily(),
                fontSize = 28.sp,
                color = Color.White,
                modifier = Modifier.padding(12.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(title, fontSize = 12.sp)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MessageDialog(title: String, content: String, clickDismiss: () -> Unit) {
    AlertDialog(onDismissRequest = {
        clickDismiss.invoke()
    }, buttons = {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = clickDismiss,
            ) {
                Text("确定")
            }
        }
    }, title = {
        Text(title)
    }, text = {
        // text默认不支持复制黏贴 需要用SelectionContainer包裹
        SelectionContainer {
            Text(content)
        }
    })
}