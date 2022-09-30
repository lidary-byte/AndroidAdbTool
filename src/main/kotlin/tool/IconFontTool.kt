package tool

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Typeface
import org.jetbrains.skia.Typeface
import org.jetbrains.skia.makeFromFile
import java.io.File

private var ttfPath =
    System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "iconfont.ttf"

fun ttfFontFamily() = FontFamily(
    Typeface(
        Typeface.makeFromFile(ttfPath)
    )
)