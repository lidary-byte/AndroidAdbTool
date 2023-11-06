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
    val command: MutableList<String> = mutableListOf(),
    var refresh: Boolean = false,
    val iconColor: Color = randomColor[Random.nextInt(
        IntRange(
            0, randomColor.size - 1
        )
    )]
)



