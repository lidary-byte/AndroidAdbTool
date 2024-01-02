package screen.home

import bean.DeviceInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import okio.buffer
import okio.source
import se.vidstige.jadb.JadbConnection
import se.vidstige.jadb.JadbDevice

/**
 * @Author : lcc
 * @CreateData : 2024/1/2
 * @Description:
 */
class HomeViewModel : ViewModel() {
    private val jAdb = JadbConnection()

    private val _devices = MutableStateFlow<List<JadbDevice>>(emptyList())


    private val _collectDeviceName = MutableStateFlow(DeviceInfo("", "", "连接设备"))

    init {
        devicesList()
    }

    fun devicesList() {
        viewModelScope.launch(Dispatchers.IO) {
            val deviceList = jAdb.devices
            devicesName(deviceList.firstOrNull())
            _devices.emit(deviceList)
        }
    }

    fun devicesName(jAdbDevice: JadbDevice?) {
        viewModelScope.launch(Dispatchers.IO) {

            val collectDeviceName =
                jAdbDevice?.executeShell("getprop ro.product.model")?.source()?.buffer()?.readUtf8()
                    ?.trimEnd() ?: "连接设备"
            _collectDeviceName.emit(DeviceInfo(collectDeviceName, "", jAdbDevice?.serial ?: ""))
        }
    }

    val collectDeviceName
        get() = _collectDeviceName.asStateFlow()

    val devices
        get() = _devices.asStateFlow()
}