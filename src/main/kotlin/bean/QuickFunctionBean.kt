package bean

import androidx.compose.ui.graphics.Color
import res.randomColor
import kotlin.random.Random
import kotlin.random.nextInt

/**
 * 快捷功能
 */
data class QuickBean(
    val title: String,
    val icon: Int,
    val commandList: MutableList<String> = mutableListOf(),
    val command: String = "",
    var refresh: Boolean = false,
    val type: Int = ADB_TYPE_OTHER,
    val iconColor: Color = randomColor[Random.nextInt(
        IntRange(
            0, randomColor.size - 1
        )
    )]
) {
    companion object {
        const val ADB_TYPE_INSTALL = 0
        const val ADB_TYPE_SHOW_DIALOG = 1
        const val ADB_TYPE_OTHER = 999
    }
}



