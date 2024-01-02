package screen.quick

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bean.QuickBean
import moe.tlaster.precompose.viewmodel.viewModel
import se.vidstige.jadb.JadbDevice
import se.vidstige.jadb.managers.PackageManager
import widget.DialogFile
import widget.MessageDialog
import widget.RunningIndicator
import java.io.File
import javax.swing.filechooser.FileNameExtensionFilter


@Composable
fun QuickScreen(device: JadbDevice?) {
    val quickModel = viewModel { QuickScreenViewModel() }

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
        InstallApkDialog(filePathDialog, device) {
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
                            Text(text = if (item.commandList.isEmpty()) "待实现..." else "")
                            Button(
                                onClick = {
                                    when (item.type) {
                                        QuickBean.ADB_TYPE_INSTALL -> showFileChooseDialog = true
                                        QuickBean.ADB_TYPE_SHOW_DIALOG -> {
                                            dialogTitle = item.title
//                                            quickModel.runExec(item.commandList)
                                            device?.execute(item.command)
                                        }

                                        else -> device?.execute(item.command)
                                    }
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


@Composable
private fun InstallApkDialog(
    filePath: String,
    device: JadbDevice?,
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
                    PackageManager(device).forceInstall(File(filePath))
                    clickDismiss.invoke()
                },
            ) {
                Text("覆盖安装")
            }
            TextButton(
                onClick = {
                    PackageManager(device).install(File(filePath))
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
