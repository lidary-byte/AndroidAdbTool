package widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.filechooser.FileSystemView

/**
 * @Author : liDaryl
 * @CreateData : 2023/11/7
 * @Description: 文件选择
 * @param multiSelection 是否多选
 */
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun DialogFile(
    mode: Dialog.Mode = Dialog.Mode.LOAD,
    title: String = "File Choose",
    multiSelection: Boolean = false,
    extensions: List<FileNameExtensionFilter> = listOf(),
    onResult: (files: List<File>) -> Unit
) {
    DisposableEffect(Unit) {
        val job = GlobalScope.launch(Dispatchers.Main) {
            JFileChooser(FileSystemView.getFileSystemView()).apply {
                dialogTitle = title
                isMultiSelectionEnabled = multiSelection
                isAcceptAllFileFilterUsed = extensions.isEmpty()
                extensions.forEach { addChoosableFileFilter(it) }

                val returned = if (mode == Dialog.Mode.LOAD) {
                    showOpenDialog(null)
                } else {
                    showSaveDialog(null)
                }
                onResult(when (returned) {
                    JFileChooser.APPROVE_OPTION -> {
                        if (mode == Dialog.Mode.LOAD) {
                            if (multiSelection) {
                                selectedFiles.filter { it.canRead() }
                            } else {
                                listOf(selectedFile)
                            }

                        } else {
                            if (!fileFilter.accept(selectedFile)) {
                                val ext = (fileFilter as FileNameExtensionFilter).extensions[0]
                                selectedFile = File(selectedFile.absolutePath + ".$ext")
                            }
                            listOf(selectedFile)
                        }
                    }

                    else -> listOf();
                })
            }

        }

        onDispose {
            job.cancel()
        }
    }
}

class Dialog {
    enum class Mode { LOAD, SAVE }
}