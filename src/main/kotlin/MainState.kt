import androidx.compose.runtime.*
import config.AppTheme
import se.vidstige.jadb.JadbConnection
import se.vidstige.jadb.JadbDevice
import se.vidstige.jadb.managers.PackageManager

/**
 * @Author : lcc
 * @CreateData : 2024/2/28
 * @Description:
 */
@Stable
class MainState {

    var theme by mutableStateOf(AppTheme.System)
    var selectDevice by mutableStateOf<JadbDevice?>(null)
    var devices by mutableStateOf<List<JadbDevice>>(emptyList())

    var selectIndex by mutableIntStateOf(0)

    var packageManager: PackageManager? = null

    init {
        refreshDevices()
    }

    fun refreshDevices() {
        devices = JadbConnection().devices
        if (devices.isNotEmpty() && selectDevice == null) {
            selectDevice = devices[0]
            packageManager = PackageManager(selectDevice)
        }
    }
}