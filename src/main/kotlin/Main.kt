// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import bean.DeviceInfo
import bean.createMainNavData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source
import page.FileManager
import page.Logcat
import page.QuickPage
import page.SettingPage
import res.defaultBgColor
import tool.AdbTool
import tool.runExec


@Composable
@Preview
fun App() {
    val adbPath = AdbTool.adbPath()
    var selectItem by remember { mutableStateOf(0) }
    // 当前选中的设备id
    var device by remember { mutableStateOf("") }
    Scaffold(bottomBar = {
        MainNav(modifier = Modifier.width(200.dp), {
            selectItem = it
        }) {
            if (!it.isNullOrEmpty()) {
                device = it
            }
        }
    }) {
        // NavigationRail 宽度默认72.dp
        Box(
            modifier = Modifier.background(defaultBgColor).fillMaxHeight().fillMaxWidth().padding(start = 200.dp)
        ) {
            when (selectItem) {
                0 -> QuickPage(device)
                1 -> FileManager(device)
                2 -> Logcat(device)
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
private fun MainNav(modifier: Modifier, onSelectItem: (Int) -> Unit, deviceId: (String?) -> Unit) {
    var navigationIndex by remember { mutableStateOf(0) }
    NavigationRail(modifier, elevation = 0.dp, header = {
        Spacer(modifier = Modifier.height(12.dp))
        ConnectDevices(deviceId)
    }) {
        createMainNavData().forEachIndexed { index, mainNavBean ->
            MainNavItem(navigationIndex, index, mainNavBean.svgName, mainNavBean.labelText) {
                navigationIndex = it
                onSelectItem.invoke(navigationIndex)
            }
        }
    }
}


/**
 * 连接设备widget
 */
@Composable
fun ConnectDevices(deviceCallback: (String?) -> Unit) {
    // 拿到已经连接的所有设备
    val devices = remember { mutableStateListOf<DeviceInfo>() }

    var refresh by remember { mutableStateOf(0) }
    LaunchedEffect(refresh) {

        withContext(Dispatchers.IO) {
            val p = Runtime.getRuntime().exec("adb devices")
            p.inputStream.source().buffer().use {
                while (true) {
                    val line = it.readUtf8Line() ?: return@use

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

                        withContext(Dispatchers.Main) {
                            devices.add(DeviceInfo(deviceName, deviceModel, device))
                        }
                    }
                }
            }
        }

    }


    var showDeviceItem by remember { mutableStateOf(false) }

    val size by animateDpAsState(
        targetValue = if (showDeviceItem) 120.dp else 0.dp
    )
    // 箭头旋转动画
    val arrowAnim by animateFloatAsState(if (showDeviceItem) -180f else 0f)

    var selectIndexDevice by remember { mutableStateOf(0) }



    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
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
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(19.dp),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Text(deviceName, color = Color.Black)
            Icon(Icons.Default.ArrowDropDown,
                contentDescription = "",
                tint = Color.Black,
                modifier = Modifier.graphicsLayer {
                    rotationX = arrowAnim
                })
        }
        Spacer(modifier = Modifier.height(12.dp))
        AnimatedVisibility(size != 0.dp) {
            LazyColumn {
                items(devices.size) {
                    val device = devices[it]
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable {
                            selectIndexDevice = it
                            showDeviceItem = false
                            deviceCallback.invoke(device.device)
                        }.padding(vertical = 6.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "${device.deviceName}  ${device.deviceModel}",
                            color = if (deviceName.contains(device.deviceName) || deviceName.contains(device.deviceModel)) Color(
                                139, 195, 74
                            ) else Color.Black
                        )
                        if (deviceName.contains(device.deviceName) || deviceName.contains(device.deviceModel)) {
                            Icon(Icons.Default.Check, contentDescription = "", tint = Color(139, 195, 74))
                        }
                    }
                }
            }
        }


        Button(onClick = {
            devices.clear()
            refresh++
        }, modifier = Modifier.fillMaxWidth()) {
            Text("刷新Adb Device")
        }
    }

}


/**
 *
 * 首页导航
 * @param selectedPosition 当前选中的position
 * @param mePosition 自己的position
 */
@Composable
private fun MainNavItem(
    selectedPosition: Int, mePosition: Int, svgPath: String, labelText: String, onClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable {
            onClick.invoke(mePosition)
        }.padding(12.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(svgPath), "", modifier = Modifier.width(24.dp).height(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            labelText, color = if (selectedPosition == mePosition) Color(
                139, 195, 74
            ) else Color.Gray
        )
    }
}