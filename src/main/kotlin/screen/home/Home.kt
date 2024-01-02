package screen.home

import ROUTER_FILE_MANAGER_SCREEN
import ROUTER_LOGCAT_SCREEN
import ROUTER_QUICK_SCREEN
import ROUTER_SETTING_SCREEN
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import bean.HomeTab
import bean.createHomeTab
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import moe.tlaster.precompose.viewmodel.viewModel
import screen.file_manager.FileMangerScreen
import screen.logcat.LogcatScreen
import screen.quick.QuickScreen
import screen.setting.SettingScreen
import se.vidstige.jadb.JadbDevice
import tool.deviceId


@Composable
fun Home() {
    val viewModel = viewModel { HomeViewModel() }

    // 当前选中的设备id
    var device by remember { mutableStateOf<JadbDevice?>(null) }

    val navigator = rememberNavigator()

    Row {
        NavigationRail(
            modifier = Modifier.width(200.dp),
            header = {
                Spacer(modifier = Modifier.height(12.dp))
                ConnectDevices(viewModel) {
                    if (it != null && it.serial != device?.serial) {
                        device = it
                    }
                }
            }) {
            createHomeTab().map {
                TabNavigationItem(it, navigator)
            }
        }

        NavHost(
            navigator = navigator,
            navTransition = NavTransition(),
            modifier = Modifier.fillMaxSize(),
            initialRoute = ROUTER_QUICK_SCREEN
        ) {
            scene(
                route = ROUTER_QUICK_SCREEN,
            ) {
                QuickScreen(device)
            }
            scene(
                route = ROUTER_FILE_MANAGER_SCREEN,
            ) {
                FileMangerScreen("")
            }
            scene(
                route = ROUTER_LOGCAT_SCREEN,
            ) {
                LogcatScreen("")
            }
            scene(
                route = ROUTER_SETTING_SCREEN,
            ) {
                SettingScreen("")
            }
        }
    }
}


@Composable
private fun TabNavigationItem(tab: HomeTab, navigator: Navigator) {
    val currentEntry by navigator.currentEntry.collectAsState(null)

    val color = if (currentEntry?.path == tab.router) Color(
        139, 195, 74
    ) else Color.Gray
    Row(
        modifier = Modifier.fillMaxWidth().clickable {
            if (currentEntry?.path == tab.router) {
                return@clickable
            }
            navigator.navigate(tab.router)
        }.padding(12.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            rememberVectorPainter(tab.icon), tab.name, modifier = Modifier.width(24.dp).height(24.dp),
            colorFilter = ColorFilter.tint(color = color)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            tab.name,
            color = color
        )
    }

}


/**
 * 连接设备widget
 */
@Composable
fun ConnectDevices(viewModel: HomeViewModel, deviceCallback: (JadbDevice?) -> Unit) {
    // 拿到已经连接的所有设备
    val devices by viewModel.devices.collectAsStateWithLifecycle()

    var showDeviceItem by remember { mutableStateOf(false) }

    val size by animateDpAsState(
        targetValue = if (showDeviceItem) 120.dp else 0.dp
    )
    // 箭头旋转动画
    val arrowAnim by animateFloatAsState(if (showDeviceItem) -180f else 0f)

    var selectIndexDevice by remember { mutableStateOf(0) }

    val connectDeviceName by viewModel.collectDeviceName.collectAsStateWithLifecycle()


    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
        // 当前连接的设备
        deviceCallback.invoke(devices.firstOrNull())

        TextButton(
            {
                showDeviceItem = !showDeviceItem
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(19.dp),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Text(connectDeviceName.deviceName, color = Color.Black)
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
                            deviceCallback.invoke(device)
                            viewModel.devicesName(device)
                        }.padding(vertical = 6.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            connectDeviceName.deviceName,
                            color = if (connectDeviceName.device == device.serial && connectDeviceName.device.isNotBlank()) Color(
                                139, 195, 74
                            ) else Color.Black
                        )
                        if (connectDeviceName.device == device.serial && connectDeviceName.device.isNotBlank()) {
                            Icon(Icons.Default.Check, contentDescription = "", tint = Color(139, 195, 74))
                        }
                    }
                }
            }
        }


        Button(onClick = {
            viewModel.devicesList()
        }, modifier = Modifier.fillMaxWidth()) {
            Text("刷新Device")
        }
    }

}

