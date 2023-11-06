package screen.quick

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bean.DeviceInfo
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.delay
import okio.buffer
import okio.source
import res.randomColor
import tool.AdbTool
import tool.deviceId
import tool.runExec
import tool.ttfFontFamily
import widget.RunningIndicator
import kotlin.random.Random
import kotlin.random.nextInt

data class QuickScreen(val device: String) : Screen {

    @Composable
    override fun Content() {
        val quickModel = rememberScreenModel { QuickScreenModel() }
        val quickData by quickModel.quickData.collectAsState()


        var dialogTitle by remember { mutableStateOf("") }
        var dialogContent by remember { mutableStateOf("") }


        if (dialogContent.isNotEmpty()) {
            MessageDialog(dialogTitle, dialogContent) {
                dialogTitle = ""
                dialogContent = ""
            }
        }
        Column {
//            CheckPackage {
//
//            }
            LazyVerticalGrid(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp),
                // 固定两列
                columns = GridCells.Adaptive(250.dp),
                content = {
                    itemsIndexed(quickData) { index, item ->
                        Card(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxHeight().padding(12.dp)
                            ) {
                                Text(text = item.title, fontWeight = FontWeight.Bold)
                                Button(
                                    onClick = {
                                        quickModel.runExec(index, item.command)
                                    }, modifier = Modifier.align(Alignment.End)
                                ) {
                                    when (item.refresh) {
                                        true -> RunningIndicator()
                                        else -> Text(text = "执行")
                                    }
                                }
                            }


                        }
                    }
                })
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


    /**
     * 连接设备widget
     */
    @Composable
    fun CheckPackage(selectPackageName: (String?) -> Unit) {
        // 拿到已经连接的所有设备
        val packageNames = remember { mutableListOf("") }

//        if (device.isBlank()) return
        LaunchedEffect(Unit) {
            delay(4000)
            val p = Runtime.getRuntime().exec(arrayOf("adb -s $deviceId shell pm list packages -f"))
            p.inputStream.source().buffer().use {
                while (true) {
                    val line = it.readUtf8Line() ?: return@use
                println("======================$line")
                    val packageName = line.split("=").lastOrNull()
                    if (packageName.isNullOrBlank()) {
                        continue
                    }
                    packageNames.add(packageName)
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
            val packageName = if (packageNames.isNotEmpty()) {
                val packageName = packageNames[selectIndexDevice]
                // 获取设备品牌
                selectPackageName.invoke(packageName)
                packageName
            } else {
                selectPackageName.invoke(null)
                "选择应用"
            }
            TextButton(
                {
                    showDeviceItem = !showDeviceItem
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(19.dp),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Text(packageName, color = Color.Black)
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
                    itemsIndexed(packageNames) { index, item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable {
                                selectIndexDevice = index
                                showDeviceItem = false
                                selectPackageName.invoke(device)
                            }.padding(vertical = 6.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                item,
//                                color = if (deviceName.contains(device.deviceName) || deviceName.contains(device.deviceModel)) Color(
//                                    139, 195, 74
//                                ) else Color.Black
                            )
//                            if (deviceName.contains(device.deviceName) || deviceName.contains(device.deviceModel)) {
//                                Icon(Icons.Default.Check, contentDescription = "", tint = Color(139, 195, 74))
//                            }
                        }
                    }

                }
            }
        }

    }

}