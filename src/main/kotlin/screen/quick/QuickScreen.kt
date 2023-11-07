package screen.quick

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bean.QuickBean
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.delay
import okio.buffer
import okio.source
import tool.deviceId
import widget.DialogFile
import widget.MessageDialog
import widget.RunningIndicator
import javax.swing.filechooser.FileNameExtensionFilter

data class QuickScreen(val device: String) : Screen {

    @Composable
    override fun Content() {
        val quickModel = rememberScreenModel { QuickScreenModel() }
        val quickData by quickModel.quickData.collectAsState()
        // adb命令执行结果
        val execResult by quickModel.execResult.collectAsState()

        // apk文件安装相关
        var showFileChooseDialog by remember { mutableStateOf(false) }
        var filePathDialog by remember { mutableStateOf("") }

        var dialogTitle by remember { mutableStateOf("") }

        if (dialogTitle.isNotBlank() && execResult.isNotBlank()) {
            MessageDialog(dialogTitle, execResult) {
                dialogTitle = ""
                quickModel.resetExecResult()
            }
        }

        // apk安装
        if (showFileChooseDialog) {
            DialogFile(title = "选择要安装的apk文件", extensions = listOf(FileNameExtensionFilter("Apk File", "apk"))) {
                if (it.isNotEmpty()) {
                    showFileChooseDialog = false
                    filePathDialog = it.first().absolutePath
                }
            }
        }

        if (filePathDialog.isNotBlank()) {
            InstallApkDialog(filePathDialog, commandFun = {
                quickModel.runExec(it)
            }) {
                filePathDialog = ""
            }
        }
        Column {
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
                                Text(text = if (item.command.isEmpty()) "待实现..." else "")
                                Button(
                                    onClick = {
                                        when (item.type) {
                                            QuickBean.ADB_TYPE_INSTALL -> showFileChooseDialog = true
                                            QuickBean.ADB_TYPE_SHOW_DIALOG -> {
                                                dialogTitle = item.title
                                                quickModel.runExec(item.command)
                                            }

                                            else -> quickModel.runExec(item.command)
                                        }
                                    }, modifier = Modifier.align(Alignment.End), enabled = item.command.isNotEmpty()
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
    private fun InstallApkDialog(
        filePath: String,
        commandFun: (commandList: MutableList<String>) -> Unit,
        clickDismiss: () -> Unit
    ) {
        AlertDialog(onDismissRequest = {
            clickDismiss.invoke()
        }, buttons = {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {
                        commandFun.invoke(mutableListOf("install", "-r", filePath))
                        clickDismiss.invoke()
                    },
                ) {
                    Text("覆盖安装")
                }
                TextButton(
                    onClick = {
                        commandFun.invoke(mutableListOf("install", filePath))
                        clickDismiss.invoke()
                    },
                ) {
                    Text("安装")
                }
            }
        }, title = {
            Text("安装应用")
        }, text = {
            // text默认不支持复制黏贴 需要用SelectionContainer包裹
            SelectionContainer {
                Text(filePath)
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