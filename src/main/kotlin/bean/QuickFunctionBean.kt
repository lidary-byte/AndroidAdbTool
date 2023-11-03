package bean

import androidx.compose.ui.graphics.Color
import res.randomColor
import kotlin.random.Random
import kotlin.random.nextInt

/**
 * 快捷功能
 */
data class QuickBean(val title: String, val child: List<QuickChildBean>)

data class QuickChildBean(
    val title: String, val icon: Int, val iconColor: Color = randomColor[Random.nextInt(
        IntRange(
            0, randomColor.size - 1
        )
    )]
)

