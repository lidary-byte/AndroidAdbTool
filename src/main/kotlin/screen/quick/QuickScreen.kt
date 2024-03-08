package screen.quick

import MainState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogWindow
import bean.TipDialog
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import config.AppIcons
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextField
import res.whiteColor
import widget.MessageDialog
import java.io.File


@Composable
fun QuickScreen(mainState: MainState) {
    val quickState = remember { QuickState(mainState) }

    var showInputDialog by remember { mutableStateOf(false) }
    var inputContent by remember { mutableStateOf("") }
    if (showInputDialog) {
        DialogWindow(onCloseRequest = {
            showInputDialog = false
        }, title = "输入文本") {
            Column(
                modifier = Modifier.fillMaxSize().background(whiteColor),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = inputContent, onValueChange = { inputContent = it }, modifier = Modifier.fillMaxWidth()
                        .padding(12.dp)
                )
                DefaultButton({
                    showInputDialog = false
                    quickState.runExec("input text $inputContent")
                    inputContent = ""
                }, modifier = Modifier.align(Alignment.CenterHorizontally).padding(12.dp)) {
                    Text("确定")
                }
            }
        }
    }



    quickState.tipDialog?.let {
        MessageDialog(it.title, it.content) {
            quickState.tipDialog = null
        }
    }


    // apk安装
    FilePicker(
        show = quickState.showFileChooseDialog,
        title = "请选择要安装的Apk",
        fileExtensions = listOf("apk")
    ) {
        quickState.showFileChooseDialog = false
        if (!it?.path.isNullOrBlank()) {
            quickState.runExecUnit {
                mainState.packageManager?.forceInstall(File(it?.path ?: ""))
            }
        }
    }



    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp),
        // 固定两列
        columns = GridCells.Fixed(4)
    ) {
        item(span = {
            GridItemSpan(4)
        }) {
            ItemHeader("常用功能", 0XFFFFC271)
        }
        item {
            GridItem("查看当前Activity", "ic_view_activity.svg", 0XFF91CF57) {
                val activityInfo = quickState.runExec {
                    mainState.selectDevice?.executeShell("dumpsys activity activities | grep -E 'mCurrentFocus|mFocusedApp'")
                        ?.bufferedReader()
                        ?.readText()?.split("mFocusedApp=")?.first()?.split("mCurrentFocus=")?.getOrNull(1)
                }
                if (!activityInfo.isNullOrBlank()) {
                    quickState.tipDialog = TipDialog("当前Activity是:", activityInfo)
                }

            }
        }
        item {
            GridItem("安装应用", "ic_install.svg", 0XFF00A8FC) {
                quickState.showFileChooseDialog = true
            }
        }
        item {
            GridItem("输入文本", "ic_input.svg", 0XFF91CF57) {
                showInputDialog = true
            }
        }


        item(span = {
            GridItemSpan(4)
        }) {
            ItemHeader("按键相关", 0XFFFFA92E)
        }
        item {
            GridItem("Home键", "ic_home.svg", 0XFF73929E) {
                quickState.runExec("input keyevent 3")
            }
        }
        item {
            GridItem("返回键", "ic_back.svg", 0xFF59C471) {
                quickState.runExec("input keyevent 4")
            }
        }
        item {
            GridItem("切换应用", "ic_switch_app.svg", 0xFFFFD43D) {
                quickState.runExec("input keyevent 187")
            }
        }
        item {
            GridItem("电源键", "ic_power.svg", 0XFF9564FF) {
                quickState.runExec("input keyevent 26")
            }
        }

        item {
            GridItem("增加音量", "ic_add_volume.svg", 0XFF00CADB) {
                quickState.runExec("input keyevent 24")
            }
        }
        item {
            GridItem("降低音量", "ic_dec_volume.svg", 0XFFFFC16F) {
                quickState.runExec("input keyevent 25")
            }
        }
        item {
            GridItem("静音", "ic_cancel_volume.svg", 0XFF73929E) {
                quickState.runExec("input keyevent 164")
            }
        }



        item(span = {
            GridItemSpan(4)
        }) {
            ItemHeader("屏幕输入", 0XFFFF2825)
        }
        item {
            GridItem("向上滑动", "ic_up.svg", 0XFF9A6AFF) {
                quickState.runExec("input swipe 300 1300 300 300")
            }
        }
        item {
            GridItem("向下滑动", "ic_down.svg", 0XFF8AA5AF) {
                quickState.runExec("input swipe 300 300 300 1300")
            }
        }
        item {
            GridItem("向左滑动", "ic_left.svg", 0XFFFF655F) {
                quickState.runExec("input swipe 900 300 100 300")
            }
        }
        item {
            GridItem("向右滑动", "ic_right.svg", 0xFFFFD43D) {
                quickState.runExec("input swipe 100 300 900 300")
            }
        }

    }
}


@Composable
fun ItemHeader(text: String, color: Long) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(
            modifier = Modifier.height(20.dp).width(4.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(color))

        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GridItem(text: String, icon: String, iconColor: Long, onClick: () -> Unit) {
    Card(
        onClick = onClick, modifier = Modifier.padding(vertical = 12.dp, horizontal = 40.dp), elevation = 0.dp
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(12.dp))
            Icon(
                icon,
                text,
                AppIcons::class.java,
                modifier = Modifier.size(44.dp),
                colorFilter = ColorFilter.tint(Color(iconColor))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = text)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}