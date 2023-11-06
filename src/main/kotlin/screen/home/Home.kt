package screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import bean.DeviceInfo
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.navigator.tab.*
import okio.buffer
import okio.source
import res.defaultBgColor
import tool.AdbTool
import tool.deviceId
import tool.runExec


@OptIn(ExperimentalVoyagerApi::class)
@Composable
fun Home() {
    val adbPath = AdbTool.adbPath()
    // 当前选中的设备id
    var device by remember { mutableStateOf("") }



    TabNavigator(QuickTab(device), tabDisposable = {
        TabDisposable(
            navigator = it,
            tabs = listOf(QuickTab(device), FileManagerTab, LogcatTab, SettingTab)
        )
    }) {
        Scaffold(
            content = {
                Box(
                    modifier = Modifier.background(defaultBgColor).fillMaxSize().padding(start = 200.dp)
                ) {
                    CurrentTab()
                }
            },
            bottomBar = {
                NavigationRail(
                    modifier = Modifier.width(200.dp),
                    header = {
                        Spacer(modifier = Modifier.height(12.dp))
                        ConnectDevices {
                            if (!it.isNullOrEmpty() && it != device) {
                                device = it
                                deviceId = device
                            }
                        }
                    }) {
                    TabNavigationItem(QuickTab(device))
                    TabNavigationItem(FileManagerTab)
                    TabNavigationItem(LogcatTab)
                    TabNavigationItem(SettingTab)
                }
            }

        )
    }

}

@Composable
private fun TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    val color = if (tabNavigator.current.key == tab.key) Color(
        139, 195, 74
    ) else Color.Gray
    Row(
        modifier = Modifier.fillMaxWidth().clickable {
            tabNavigator.current = tab
        }.padding(12.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            tab.options.icon!!, tab.options.title, modifier = Modifier.width(24.dp).height(24.dp),
            colorFilter = ColorFilter.tint(color = color)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            tab.options.title, color = color
        )
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
                    devices.add(DeviceInfo(deviceName, deviceModel, device))
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
            Icon(
                Icons.Default.ArrowDropDown,
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
            Text("刷新Device")
        }
    }

}

