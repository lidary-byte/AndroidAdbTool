package widget

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow

/**
 * @Author : lidary
 * @CreateData : 2023/11/7
 * @Description:
 */

@Composable
fun MessageDialog(title: String, content: String, clickDismiss: () -> Unit) {
    DialogWindow(onCloseRequest = clickDismiss, title = title) {
        // text默认不支持复制黏贴 需要用SelectionContainer包裹
        SelectionContainer(
            modifier = Modifier.fillMaxHeight(1f).fillMaxWidth().padding(16.dp).verticalScroll(rememberScrollState())
        ) {
            Text(content)
        }
    }
    // AlertDialog中Content设置为可滚动类型时有bug
//    AlertDialog(onDismissRequest = {
//        clickDismiss.invoke()
//    }, buttons = {
//        Row(
//            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
//        ) {
//            TextButton(
//                onClick = clickDismiss,
//            ) {
//                Text("确定")
//            }
//        }
//    }, title = {
//        Text(title)
//    }, text = {
//        // text默认不支持复制黏贴 需要用SelectionContainer包裹
//        SelectionContainer {
//            Text(content)
//        }
//    })
}